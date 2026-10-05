package org.httpserver;

import org.httpserver.http.httpConstant.RouteHandler;
import org.httpserver.http.httpConstant.Verb;
import org.httpserver.http.MyHttpServer;
import org.httpserver.http.routeur.Register;
import org.httpserver.http.routeur.Route;
import org.httpserver.http.routeur.Routeur;
import org.httpserver.shop.controller.CategoryController;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        MyHttpServer httpServer = new MyHttpServer(8080);
        Routeur routeur = createRouteur();

        try{
            httpServer.start(routeur);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static Routeur createRouteur(){
        List<Route> routes = List.of(
            new Route(Verb.GET, "/categories/", RouteHandler.LIST_ALL_CATEGORIES),
            new Route(Verb.GET, "/categories/{category}", RouteHandler.LIST_ALL_CATEGORIES)
        );

        return Routeur.createSingletonRouteur(new Register(routes));
    }
}