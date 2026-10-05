package org.httpserver.shop.service;

import org.httpserver.http.httpHelper.HelperFormHandler;
import org.httpserver.http.request.MyHttpRequest;
import org.httpserver.http.response.MyHttpResponse;

import java.util.Map;

public class CategoryService {
    public static MyHttpResponse findAll(MyHttpRequest httpRequest, Map<String, String> requestParam){
        // TODO CODE METIER
        // 1. Demander a repository, de demander a la BDD SELECT *
        // 2. Créer le code java.
        // 3. Créer la réponse http
        // 4. retourne la réponse HTTP
        return null;
    }

    public static MyHttpResponse findByCategoryName(MyHttpRequest httpRequest, Map<String, String> requestParam){
        // TODO CODE METIER
        // 1. Vérifier si les params existe bien en BDD via repository.
        // 2. Demander les données a la BDD, via CategoryRepository.
        // 3. Créer la réponse http
        // 4. Retourner la réponse http
        return null;
    }

}
