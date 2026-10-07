package org.httpserver.http.httpConstant.deprecated;

import org.httpserver.deprecated.handlerDeprecated.firstway.HandlerInterface;
import org.httpserver.deprecated.handlerDeprecated.firstway.ListAllCategories;
import org.httpserver.http.request.MyHttpRequest;
import org.httpserver.http.response.MyHttpResponse;

public enum Handler {
    NON_USING(new ListAllCategories()),
    // Pour le moment on fait simple. stockage en dur.
    LIST_ALL_CATEGORIES(new ListAllCategories());

    // ici handler est un champs de classe. il est de type Function<MyHttpRequest, MyHttpResponse> et s'appelle handler.
    final HandlerInterface handler;

    // Ici on a un constructeur, qui prends en argument un handler qui est une fonction entré MyHttpRequest et sortie httpResponse
    Handler(HandlerInterface handler){
        // Le constructeur set le handler.
        this.handler = handler;
    }

    // TODO Dessiner et retracer le chemin.
    public void start(MyHttpRequest request){
    }
}
