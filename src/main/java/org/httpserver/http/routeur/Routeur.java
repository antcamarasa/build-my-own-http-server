package org.httpserver.http.routeur;

import org.httpserver.http.httpConstant.DynamicParam;
import org.httpserver.http.httpConstant.RouteHandler;
import org.httpserver.http.httpConstant.SegmentStatus;
import org.httpserver.http.httpConstant.deprecated.Handler;
import org.httpserver.http.httpConstant.Verb;
import org.httpserver.http.httpHelper.HelperFormHandler;
import org.httpserver.http.request.MyHttpRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Routeur {
    /*
        Reflection -> un routeur est unique dans tous mon projet donc, pour éviter d'en créer plusieurs singleton ?
        Un routeur est une classe qui contient un registre.
    */
    static Routeur routeur;
    Register register;

    private Routeur(Register register){
        this.register = register;
    }

    public static Routeur createSingletonRouteur(Register register){
        if(routeur == null){
            routeur = new Routeur(register);
        }
        return routeur;
    }

    public List<Route> getAllRoutes(){return this.register.getRoutes();}
    public RouteHandler getHandler(Verb verb, String path){
        return register.containsRoute(verb, path);
    }

    public Register getRegister(){return this.register;}

    // Methode pour trouver une route.
    public HelperFormHandler findRoutes(MyHttpRequest httpRequest){
        var toto = httpRequest.getPath().split("/");


        List<Route> filterRoute = getAllRoutes()
                .stream()
                .filter( route -> route.getVerb().equals(Verb.getVerb(httpRequest.getMethod())))
                .filter( route -> route.getMotif().length == httpRequest.getPath().split("/").length)
                .toList();

        if(filterRoute.isEmpty()) return null;
        return checkRouteAndReturnHelperForHandler(httpRequest.getPath().split("/"), filterRoute);
    }

    // TODO -> A DEBUGGER
    private HelperFormHandler checkRouteAndReturnHelperForHandler(String[] path, List<Route> routes){
        boolean founded = true;
        Route foundRoute = null;
        Map<DynamicParam, String> handlerMapHelper = new HashMap<>();


        for(Route route : routes){
            for(int i = 0; i < route.getMotif().length; i++){
                // 1. Vérifie si le path est dynamic ou non.
                if(route.getMotif()[i].segmentStatus() == SegmentStatus.FIXE){
                    if(!route.getMotif()[i].value().equals(path[i])) {
                        founded = false;
                        handlerMapHelper = new HashMap<>();
                        break;}
                }
                else {
                    DynamicParam dynamicParam = DynamicParam.getEnumFromValue(route.getMotif()[i].value());
                    handlerMapHelper.put(dynamicParam, path[i]);
                }
            }
            if(founded){
                foundRoute = route;
                break;
            }
        }
        return new HelperFormHandler(handlerMapHelper, foundRoute);
    }
}
