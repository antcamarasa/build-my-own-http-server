package org.httpserver.http.routeur;

import org.httpserver.http.httpConstant.RouteHandler;
import org.httpserver.http.httpConstant.SegmentStatus;
import org.httpserver.http.httpConstant.Verb;


public class Route {
    public record template(SegmentStatus segmentStatus, String value){}
    public template[] motif;

    Verb verb;
    String path;
    RouteHandler handler;

    public Route(Verb verb, String path, RouteHandler handler){
        this.verb      = verb;
        this.path      = path;
        this.handler   = handler;
        buildMotif(path);
    }

    //________________________________________
    //________________________________  Getter
    public Verb getVerb(){
        return this.verb;
    }
    public String getPath(){
        return this.path;
    }
    public RouteHandler getHandler(){
        return this.handler;
    }
    public template[] getMotif(){return this.motif;}



    //________________________________________
    //________________________________  Helper
    public void buildMotif(String path){
        String[] splitPath = path.split("/");
        this.motif = new template[splitPath.length]; // Cela crée un tableau de splitLength de type template;

        for(int i = 0; i < splitPath.length; i++){
            String current = splitPath[i];

            SegmentStatus segmentStatus = !current.isEmpty() && current.charAt(0) == '{' && current.charAt(current.length() - 1) == '}'
                    ? SegmentStatus.DYNAMIC : SegmentStatus.FIXE;

            current = segmentStatus ==SegmentStatus.DYNAMIC
                    ?  current.substring(1, current.length() - 1)
                    :  current;

            motif[i] = new template(segmentStatus, current);
        }
    }
    public String[] splitPath(){
        return this.path.split("/");
    }
}
