package org.httpserver.http.routeur;

import org.httpserver.http.httpConstant.RouteHandler;
import org.httpserver.http.httpConstant.deprecated.Handler;
import org.httpserver.http.httpConstant.Verb;

import java.util.List;

public class Register {
    List<Route> routes;
    public Register(List<Route> routes){
        this.routes = routes;
    }

    // ____________________________________
    // _____________________________ Getter
    // TODO non bloquant : retourner une liste non modifiable.
    public List<Route> getRoutes(){
        return this.routes;
    }

    // _____________________________________
    // _______________________ Working logic
    public RouteHandler containsRoute(Verb verb, String path){
        for(Route route : routes){
            if(route.getVerb().equals(verb) && route.getPath().equals(path)){
                return route.getHandler();
            }
        }
        return null;
    }
    public void addRoute(Route route){
        this.routes.add(route);
    }
}
