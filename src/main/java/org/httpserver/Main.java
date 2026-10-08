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

        try{
            httpServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}