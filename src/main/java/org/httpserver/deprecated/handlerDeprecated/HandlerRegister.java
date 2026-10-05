package org.httpserver.deprecated.handlerDeprecated;

import org.httpserver.http.httpConstant.Status;
import org.httpserver.http.request.MyHttpRequest;
import org.httpserver.http.response.MyHttpResponse;

import java.util.HashMap;
import java.util.Map;

public class HandlerRegister {
   public static MyHttpResponse listAllCategories(MyHttpRequest httpRequest, Map<String, String> param){
       return new MyHttpResponse(Status.OK, new HashMap<>(), "listAllCategories");
   }
   public static MyHttpResponse listAllByCategory(MyHttpRequest httpRequest, Map<String, String> param){
       // 1. Vérifier que tous les param sont valides.
       // key : category | value : FRUITS
       // Comment est-ce que je vérifie cela? en BDD ?

       // Ici je dois vérifier value existe dans catégorie?
       // Param invalide -> retourne statut du code erreur 400
       // Param valide   -> je continue

       // 2. Je peux faire ma requête JDBC ?
       // SELECT param.get("category) FROM CATEGORY

       // 3. Récuper le resulats de JDBC
       return new MyHttpResponse(Status.OK, new HashMap<>(), "");
   }
}
