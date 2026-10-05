import org.httpserver.http.request.MyHttpRequest;
import org.httpserver.http.MyHttpServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TestMyHttpServer {
    MyHttpServer httpServer;
    final int PORT = 8080;
    String getRequest;
    String postRequest;


    // Je declare un socket custom ainsi qu'un inputStream Custom pour réaliser mes tests.
    private static class MyCustomSocketClass extends Socket{
        private InputStream customInputStream;

        public MyCustomSocketClass(InputStream customInputStream){
            this.customInputStream = customInputStream;
        }

        @Override
        public InputStream getInputStream(){
            return this.customInputStream;
        }
    }
    private static class MyCustomInputStreamAlwaysReturnMinusOne extends InputStream{
        @Override
        public int read() throws IOException {
            return -1;
        }
    }
    private static class MyCustomInputStreamByteArray extends InputStream{
        // Ici je dois pouvoir déroulé les éléments et savoir ou est-ce qu'on est ?
        final byte[] byteArray;
        int currentIndex = 0;

        public MyCustomInputStreamByteArray(byte[] byteArray){
            this.byteArray = byteArray;
        }

        @Override
        public int read() throws IOException {
            if(currentIndex < byteArray.length){
                return Byte.toUnsignedInt(byteArray[currentIndex++]); // on retourne est on incrémente.
            }
            return -1; // Si on a terminé de parcours notre collection.
        }
    }
    private static class MyCustomInputStreamMultiArray extends InputStream{
        List<byte[]> lstOfStreamResponse;
        int lstIndex;
        int byteIndex;


        public MyCustomInputStreamMultiArray(List<byte[]> lstOfStreamResponse){
            this.lstOfStreamResponse = lstOfStreamResponse;
            lstIndex = 0;
            byteIndex = 0;
        }

        @Override
        public int read(){
            byte[] current = lstOfStreamResponse.get(lstIndex);

            if(byteIndex < current.length){
                // c'est faux ce return read retourne un counter.
                return Byte.toUnsignedInt(current[byteIndex++]);
            }
            return -1;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            if(lstIndex >= lstOfStreamResponse.size()) return -1;

            // lstOfStreamResponse
            // 1 l'index => lstIndex - qui correspond au tableau de byte sur lequel je me positionne.

            int counter = 0; // Nombre d'élément réélement copié de destination dans b.
            int indexToInsert = off; // On insert dans b a partir de l'index off.


            // Tant que il reste de la place dans b & reste des élements dans le morceau courant
            // -> On insérer b[indexToInsert] = (byte) read() // Qui increment byteIndex.
            // -> on incrémente counter.
            while (indexToInsert < len + off && byteIndex < lstOfStreamResponse.get(lstIndex).length){
                b[indexToInsert++]= (byte) read(); // me retoune le byte lu et incrément indexByte source
                // L'intérêt est de stocker dans b, le byte de source(stream).
                counter++;
            }

            // Deux options
            // 1. La source a été vidé ?
            if(byteIndex >= lstOfStreamResponse.get(lstIndex).length){
                lstIndex = lstIndex + 1; // Attention  a ne pas sortir du scope
                byteIndex = 0; // On remet a 0 l'index de source.
            }

            // 2. Plus de place pour remplir b.
            if(indexToInsert >= len + off){ // buffer est plein on ne peut plus rien stocker on ne fait rien.
            }


            return counter;
        }
    }


    @BeforeEach
    public void setUp() throws IOException {
        this.httpServer = new MyHttpServer(PORT);
        this.getRequest = "GET /categories/FRUIT HTTP/1.1\r\n"
                + "Host: localhost:8080\r\n"
                + "User-Agent: curl/8.5.0\r\n"
                + "Accept: application/json\r\n"
                + "\r\n";

        this.postRequest = "POST /categories/FRUIT HTTP/1.1\r\n"
                + "Host: localhost:8080\r\n"
                + "User-Agent: curl/8.5.0\r\n"
                + "Accept: */*\r\n"
                + "Content-Type: application/json\r\n"
                + "Content-Length: 27\r\n"
                + "\r\n"
                + "{\"nom\":\"Pomme\",\"prix\":1.50}";
    }

    @Test
    public void shouldReturnMinusOneRefillBucket() throws IOException{
        Socket customSocket = new MyCustomSocketClass(new MyCustomInputStreamAlwaysReturnMinusOne());
        Assertions.assertEquals(-1, httpServer.reFillBucket(customSocket.getInputStream(), this.httpServer.buffer));
    }

    @Test
    public void shouldReturnNullIfRefillBucketIsMinusOne() throws IOException {
        Socket customSocket = new MyCustomSocketClass(new MyCustomInputStreamAlwaysReturnMinusOne());
        Assertions.assertEquals(null, this.httpServer.buildRequest(customSocket));
    }

    @Test
    public void simpleGetWithoutBodyInOneLecture() throws IOException {
        byte[] byteArray = this.getRequest.getBytes(StandardCharsets.UTF_8);

        // 3. et la donner a un byteArrayInputStream()
        MyCustomInputStreamByteArray myCustomInputStreamByteArray = new MyCustomInputStreamByteArray(byteArray);
        Socket socket = new MyCustomSocketClass(myCustomInputStreamByteArray);

        // 4. ...
        MyHttpRequest httpRequest = httpServer.buildRequest(socket);

        Assertions.assertEquals("GET", httpRequest.getMethod());
        Assertions.assertEquals("/categories/FRUIT", httpRequest.getPath());
        Assertions.assertEquals("HTTP/1.1", httpRequest.getVersion());
        Assertions.assertEquals(3, httpRequest.getHeaders().size());
        Assertions.assertEquals("application/json", httpRequest.getHeaders().get("Accept"));
        Assertions.assertEquals("curl/8.5.0", httpRequest.getHeaders().get("User-Agent"));
        Assertions.assertEquals("localhost:8080", httpRequest.getHeaders().get("Host"));
    }

    @Test
    public void simplePostWithBodyInOneLecture() throws IOException {
        byte[] byteArray = this.postRequest.getBytes(StandardCharsets.UTF_8);

        MyCustomInputStreamByteArray myCustomInputStreamByteArray = new MyCustomInputStreamByteArray(byteArray);
        Socket socket = new MyCustomSocketClass(myCustomInputStreamByteArray);

        MyHttpRequest httpRequest = httpServer.buildRequest(socket);

        Assertions.assertEquals("POST", httpRequest.getMethod());
        Assertions.assertEquals("/categories/FRUIT", httpRequest.getPath());
        Assertions.assertEquals("HTTP/1.1", httpRequest.getVersion());
        Assertions.assertEquals(5, httpRequest.getHeaders().size());
        Assertions.assertEquals("27", httpRequest.getHeaders().get("Content-Length"));

        String expected = "{\"nom\":\"Pomme\",\"prix\":1.50}";
        Assertions.assertEquals(expected, httpRequest.getBody());
    }

    @Test
    public void simpleGetWithoutBodyInTwoLecture() throws IOException{
        List<byte[]> lstByteArray = new ArrayList<>();

        // 1. Je convert mon texte brut en tableau d'octet.
        byte[] byteArray = this.getRequest.getBytes(StandardCharsets.UTF_8);

        // 2. Je coupe ma requête en deux.
        int length = byteArray.length;

        lstByteArray.add(createByteArray(0, length / 2, byteArray));
        lstByteArray.add(createByteArray(length /2, length, byteArray));

        MyHttpRequest httpRequest = this.httpServer.buildRequest(new MyCustomSocketClass(new MyCustomInputStreamMultiArray(lstByteArray)));
        Assertions.assertEquals("GET", httpRequest.getMethod());
        Assertions.assertEquals("/categories/FRUIT", httpRequest.getPath());
        Assertions.assertEquals("HTTP/1.1", httpRequest.getVersion());
        Assertions.assertEquals(3, httpRequest.getHeaders().size());
        Assertions.assertEquals("application/json", httpRequest.getHeaders().get("Accept"));
        Assertions.assertEquals("curl/8.5.0", httpRequest.getHeaders().get("User-Agent"));
        Assertions.assertEquals("localhost:8080", httpRequest.getHeaders().get("Host"));
    }

    @Test
    public void simplePostWithoutBodyInTwoLecture() throws IOException{
        List<byte[]> lstByteArray = new ArrayList<>();
        byte[] byteArray = this.postRequest.getBytes(StandardCharsets.UTF_8);

        int length = byteArray.length;

        lstByteArray.add(createByteArray(0, length / 2, byteArray));
        lstByteArray.add(createByteArray(length /2, length, byteArray));

        MyHttpRequest httpRequest = this.httpServer.buildRequest(new MyCustomSocketClass(new MyCustomInputStreamMultiArray(lstByteArray)));
        System.out.println(httpRequest);


        Assertions.assertEquals("POST", httpRequest.getMethod());
        Assertions.assertEquals("/categories/FRUIT", httpRequest.getPath());
        Assertions.assertEquals("HTTP/1.1", httpRequest.getVersion());
        Assertions.assertEquals(5, httpRequest.getHeaders().size());
        Assertions.assertEquals("27", httpRequest.getHeaders().get("Content-Length"));

        String expected = "{\"nom\":\"Pomme\",\"prix\":1.50}";
        Assertions.assertEquals(expected, httpRequest.getBody());

    }

    // ___________________________________ Helper for test _____________________________________
    byte[] createByteArray(int start, int end, byte[] source){
        byte[] byteArray = new byte[end - start];

        for(int i = start; i < end; i++){
            byteArray[i - start] = source[i];
        }

        return byteArray;
    }
}
