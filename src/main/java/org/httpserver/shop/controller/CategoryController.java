package org.httpserver.shop.controller;

import org.httpserver.http.request.MyHttpRequest;
import org.httpserver.http.response.MyHttpResponse;
import org.httpserver.shop.service.CategoryService;

import java.util.Map;


public class CategoryController {

    // Get -> /categories
    public static MyHttpResponse all(MyHttpRequest httpRequest, Map<String, String> requestParam){
        CategoryService.findAll(httpRequest, requestParam);
        return null;
    }

    // Get -> /category/{category}
    public static MyHttpResponse getAllFromCategory(MyHttpRequest httpRequest, Map<String, String> requestParam){
        CategoryService.findByCategoryName(httpRequest, requestParam);
        return null;
    }
}
