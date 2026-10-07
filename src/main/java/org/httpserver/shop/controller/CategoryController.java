package org.httpserver.shop.controller;

import org.httpserver.http.httpConstant.DynamicParam;
import org.httpserver.http.request.MyHttpRequest;
import org.httpserver.http.response.MyHttpResponse;
import org.httpserver.shop.model.Product;
import org.httpserver.shop.service.CategoryService;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public class CategoryController {

    // Get -> /categories
    public static MyHttpResponse all(MyHttpRequest httpRequest, Map<DynamicParam, String> requestParam) throws SQLException {
        CategoryService.findAll();
        return null;
    }

    // Get -> /category/{category}
    public static MyHttpResponse getAllFromCategory(MyHttpRequest httpRequest, Map<DynamicParam, String> requestParam){

        String categoryValue = requestParam.get(DynamicParam.CATEGORY);

        if(categoryValue == null  && categoryValue.isBlank()){
            // Erreur 400
            throw new RuntimeException("request param is null or empty");
        }

        List<Product> products =  CategoryService.findProductsByCategoryName(categoryValue);
        for(Product product : products){
            System.out.println("Name : " + product.getName());
            System.out.println("Prix : " + product.getPrice());
        }
        // Construire la requête http.
        return null;
    }
}
