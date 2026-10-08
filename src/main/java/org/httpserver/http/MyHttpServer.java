package org.httpserver.http;

import org.httpserver.http.httpConstant.DynamicParam;
import org.httpserver.http.httpConstant.RouteHandler;
import org.httpserver.http.httpConstant.SegmentStatus;
import org.httpserver.http.httpConstant.Verb;
import org.httpserver.http.httpHelper.HelperFormHandler;
import org.httpserver.http.request.MyHttpRequest;
import org.httpserver.http.request.MyHttpRequestBuilder;
import org.httpserver.http.response.MyHttpResponse;
import org.httpserver.http.routeur.Register;
import org.httpserver.http.routeur.Route;
import org.httpserver.http.routeur.RouteConfig;
import org.httpserver.http.routeur.Routeur;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

    public class MyHttpServer {
        public ServerSocket serverSocket;
        Routeur routeur;
        MyHttpRequest httpRequest;
        MyHttpResponse httpResponse;

        final int port;
        public final int BYTE_MAX_SIZE = 8192 ;
        final int SYMBOL_LENGTH = 4;
        public byte[] buffer = new byte[BYTE_MAX_SIZE];

        record HelperResponse(boolean isHeaderComplete, Integer nextIndex) {}
        record HeaderLineResult(int nextIndex, boolean headerIsComplete) {}
        record HelperForBody(Integer startBodyIndex, Integer bodyLength){}

        public MyHttpServer(int port){
            this.port = port;
        }

        public void start() throws IOException {
            this.serverSocket = new ServerSocket(port);
            routeur = new RouteConfig().getRouteur();
            handleConnection();
        }

        public void handleConnection() throws IOException {
            while(true){
                Socket socket = this.serverSocket.accept();
                httpRequest   = buildRequest(socket);

                // Contient un record, contenant la route et les param dynamic si existant.
                HelperFormHandler handlerParam = routeur.findRoutes(httpRequest);

                // Je récupère le handler lié a ma route.
                RouteHandler handler = handlerParam.route().getHandler();

                // J'apply la méthode contenu dans le champs handler de ma route.
                // En paramètre : httpRequest et params dynamic si existant.

                // apply appel mon controller, avec la bonne méthode. important chaque méthode dans mes controller ont
                // la meme entrée est la meme sortie > httpRequest, Map<String, String> param dynamic -> httpResponse.
                handler.getHandlerMethod().apply(httpRequest, handlerParam.requestParam());
            }
        }


        // ______________________________________________ BUILD REQUEST ________________________________________________
        // Parser from octet to httpRequest object
        public MyHttpRequest buildRequest(Socket socket) throws IOException {
            // 1. Branche le stream. a vérifier pourquoi il faut gérer une exception ?
            InputStream inputStream = socket.getInputStream();
            MyHttpRequestBuilder requestBuilder = new MyHttpRequestBuilder();

            // 2. rempli moi le bucket
            int indexOfLastUse = reFillBucket(inputStream, buffer);
            if(indexOfLastUse == -1) return null; // Client close the connection.

            // 2. construire le headers
            HelperForBody helperForBody= buildHeaders(requestBuilder, inputStream, buffer, indexOfLastUse);

            // 3. Construire le body
            buildBody(requestBuilder, inputStream, buffer, helperForBody.startBodyIndex, indexOfLastUse, helperForBody.bodyLength);

            // 4. retourner la requête builder.
            return requestBuilder.build();
        }

        private HelperForBody buildHeaders(MyHttpRequestBuilder httpRequestBuilder, InputStream stream, byte[] buffer, int indexOfLastUse) throws IOException {
            Map<String, String> headers = new HashMap<>();
            List<String> lines = new ArrayList<>();
            StringBuilder line = new StringBuilder();

            boolean reFillIsNeeded = false;
            boolean headerIsComplete = false;

            Integer cloneIndexBeginning = null;
            Integer bodyStartIndex = null;

            while (!headerIsComplete){
                if(reFillIsNeeded)
                {
                    indexOfLastUse = compact(cloneIndexBeginning, indexOfLastUse, stream, buffer);
                    reFillIsNeeded = false;
                    cloneIndexBeginning = null;
                }

                for(int i = 0; i < indexOfLastUse; i++){
                    if(buffer[i] == 13){
                        // Strictement ingférieur car on commence a 0 donc le dernier élément n'est pas inclus.
                        if(i + 3 < indexOfLastUse){
                            // Quoi qu'il arrive soit next line, soit header is ended.
                            HeaderLineResult headerLineResult = isHeaderComplete(buffer, i);
                            headerIsComplete = headerLineResult.headerIsComplete();
                            i = headerLineResult.nextIndex() - 1; // Obligation car on est dans une boucle for(i++);

                            if(headerIsComplete){
                                bodyStartIndex = headerLineResult.nextIndex;
                            }
                            // Ajoute la nouvelle ligne.
                            lines.add(line.toString());
                            line = new StringBuilder();
                        }
                        else
                        {
                            reFillIsNeeded = true;
                            cloneIndexBeginning = i;
                        }
                    }
                    else{
                        line.append((char) buffer[i]);
                    }
                }
                // Si on sort de la boucle.
                reFillIsNeeded = true;
            }


            fillHttpRequestBuildWithHeaders(httpRequestBuilder, headers, lines);
            Integer bodyContentLength = getBodyContentLength(headers);
            return new HelperForBody(bodyStartIndex, bodyContentLength);
        }
        private void buildBody(MyHttpRequestBuilder builder, InputStream stream, byte[] buffer, int bodyStartIndex, int lastUsedElement, Integer bodyLength){
            if(bodyLength == null){
                builder.withBody("");
                return;
            }

            lastUsedElement = bodyStartIndex + bodyLength;

            StringBuilder sb = new StringBuilder();
            int elementOfBodyToRead = bodyLength;

            while (elementOfBodyToRead > 0){
                for(int i = bodyStartIndex; i < lastUsedElement; i++){
                    sb.append((char)buffer[i]);
                    elementOfBodyToRead--;
                }

                if(elementOfBodyToRead > 0){
                    int readElement = reFillBucket(stream, buffer);
                    if(readElement == -1) throw new RuntimeException("Error");
                    bodyStartIndex = 0;
                    lastUsedElement = readElement;
                }
            }
            builder.withBody(sb.toString());
        }

        public int reFillBucket(InputStream inputStream, byte[] buffer){
            try {
                return inputStream.read(buffer);
            } catch (Exception e){
                throw new RuntimeException("Impossible to get data from socket.");
            }
        }
        private int compact(Integer startIndex, Integer endIndex,  InputStream stream, byte[] buffer)throws IOException{
            int helperIndex = 0;
            if(startIndex != null && endIndex != null){
                for(int i = startIndex; i < endIndex; i++){
                    buffer[helperIndex] = buffer[i];
                    helperIndex = helperIndex + 1;
                }
            }

            int readValue =  stream.read(buffer, helperIndex, BYTE_MAX_SIZE - helperIndex);
            return readValue == -1 ? -1 : readValue + helperIndex;
        }
        private HeaderLineResult isHeaderComplete(byte[] buffer, int index){
            // 17 - 18 - 19 - 20 | 21 STOP.
            int counter = 0;
            for(int i = index; i < index + SYMBOL_LENGTH; i++){
                if(buffer[i] == 13 || buffer[i] == 10){
                    counter = counter + 1;
                    continue;
                }
                break;
            }
            return new HeaderLineResult(
                    index + counter,
                    counter == 4
            );
        }
        private void fillHttpRequestBuildWithHeaders(MyHttpRequestBuilder httpRequestBuilder, Map<String, String> headers, List<String> lines){
            for(int i = 0; i < lines.size(); i++){
                if(i == 0){
                    String[] splitLine = lines.get(i).split(" ", 3);
                    httpRequestBuilder.withMethod(splitLine[0]);
                    httpRequestBuilder.withPath(splitLine[1]);
                    httpRequestBuilder.withVersion(splitLine[2]);
                    continue;
                }
                String[] splitLine = lines.get(i).split(": ", 2);
                headers.put(splitLine[0], splitLine[1]);
            }
            httpRequestBuilder.withHeaders(headers);
        }
        private Integer getBodyContentLength(Map<String, String> headers){
            for(Map.Entry<String, String> header : headers.entrySet()){
                if(header.getKey().equals("Content-Length")){
                    return Integer.parseInt(header.getValue());
                }
            }
            return null;
        }

        // _____________________________________________ FIND ROUTE ____________________________________________________
        public Route findRoute(MyHttpRequest httpRequest, List<Route> routes){
            // Pour trouver la bonne route, il me faut deux choses :
            // 1. Le verb
            // 2. Le path

            Verb verb = Verb.valueOf(httpRequest.getMethod().toUpperCase());
            String[] path = httpRequest.getPath().split("/");
            Route matchRoute = null;

            boolean routFounded = false;
            //Route route : routeur.getRegister().getRoutes()
            for(Route route : routes){
                if(route.getVerb() == verb){
                   String[] routePath = route.splitPath();
                   if(routePath.length != path.length) continue;


                   boolean wordMatch = true;
                   for(int word = 0; word < routePath.length; word++){
                       String currentRouteWord = routePath[word];
                       String currentHttpRequestPathWord = path[word];

                       if(!currentRouteWord.isEmpty() && currentRouteWord.charAt(0) != '{' && currentRouteWord.length() != currentHttpRequestPathWord.length()){
                           return null;
                       }

                       for(int charInWord = 0; charInWord < currentRouteWord.length(); charInWord++){
                           // Je vérifie si j'ai un param dynamique { }, dans ce cas je continue sur le prochain mot.
                           if(currentRouteWord.charAt(charInWord) == '{' && currentRouteWord.charAt(currentRouteWord.length() - 1) == '}'){
                               break;
                           }
                           if(currentRouteWord.charAt(charInWord) != currentHttpRequestPathWord.charAt(charInWord)){
                               wordMatch = false;
                               break;
                           }
                       }
                       if(!wordMatch){
                           break;
                       }
                   }

                   if(wordMatch){
                       matchRoute= route;
                   }
                }
            }

            return matchRoute;
        }
        public Route findRouteStream(MyHttpRequest httpRequest, List<Route> routes){
            // 1. Filter by verb

            String[] input = httpRequest.getPath().split("/");

            List<Route> routePreMatch = routes.stream()
                    .filter(route -> route.getVerb().equals(Verb.getVerb(httpRequest.getMethod())))
                    .filter(route -> route.splitPath().length == input.length).toList();


            if(routePreMatch.size() == 1) return routePreMatch.getFirst();


            return compareAllCharacterInPath(input, routePreMatch);
        }
        private Route compareAllCharacterInPath(String[] input, List<Route> routePreMatch){
            // The goal is to compare each element of routePreMatch and Input
            Route foundedRoute = null;

            for(Route route : routePreMatch){
                String[] path = route.getPath().split("/");

                int index = 0;
                boolean matchRoute = true;
                for(String itemInPath : path){
                    if(itemInPath.charAt(0) == '{' && itemInPath.charAt(itemInPath.length() - 1) == '}') continue;
                    if(!itemInPath.equals(input[index])){
                      matchRoute = false;
                      break;
                    };
                    index++;
                }

                foundedRoute = matchRoute ? route : null;
            }

            return foundedRoute;
        }


        // _____________________________________________ BUILD RESPONSE ________________________________________________
        // Parser from httpResponse to octet.
    }


