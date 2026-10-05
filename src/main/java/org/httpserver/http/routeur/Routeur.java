package org.httpserver.http.routeur;

import org.httpserver.http.httpConstant.deprecated.Handler;
import org.httpserver.http.httpConstant.Verb;

import java.util.List;

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

    public void addRoute(Route route){
        this.register.addRoute(route);
    }

    public Handler getHandler(Verb verb, String path){
        return register.containsRoute(verb, path);
    }
    public List<Route> getAllRoutes(){return this.register.getRoutes();}
    public Register getRegister(){
        return this.register;
    }
}
