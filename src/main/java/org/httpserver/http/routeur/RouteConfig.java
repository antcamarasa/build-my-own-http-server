package org.httpserver.http.routeur;

import org.httpserver.http.httpConstant.RouteHandler;
import org.httpserver.http.httpConstant.Verb;

import java.util.ArrayList;
import java.util.List;

public class RouteConfig {
    Routeur  routeur;
    static Register register;

    public RouteConfig(){
        register = new Register(generateAllRoute());
        routeur = Routeur.createSingletonRouteur(register);
    }

    private List<Route> generateAllRoute(){
        List<Route> routes = List.of(
          // 1. Lister toutes les categories
          new Route(Verb.GET, "/categories", RouteHandler.LIST_ALL_CATEGORIES),

          // 2. Lister une catégorie
          new Route(Verb.GET, "/categories/{id}", RouteHandler.LIST_CATEGORY),

          // 3. Lister tous les produits d'une catégorie
          new Route(Verb.GET, "/categories/{categoryId}/products", RouteHandler.LIST_ALL_PRODUCTS_OF_CATEGORY_ID)

          // 4. Créer une nouvelle category
          //new Route(Verb.POST, "categories", RouteHandler.CREATE_CATEGORY),

          // 5. Modifier une category
          //new Route(Verb.PUT, "categories/{id}", RouteHandler.UPDATE_CATEGORY),

          // 6. Supprimer une category
          //new Route(Verb.DELETE, "categories/{id}", RouteHandler.DELETE_CATEGORY)
        );

        return routes;
    }

    public Routeur getRouteur(){
        return this.routeur;
    }
}
