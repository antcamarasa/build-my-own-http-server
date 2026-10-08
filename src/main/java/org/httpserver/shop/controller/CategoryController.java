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

    // GET -> /category
    public static MyHttpResponse getCategory(MyHttpRequest httpRequest, Map<DynamicParam, String> requestParam) throws SQLException {
        CategoryService.findOne();
        return null;
    }

    // Get -> /categories
    public static MyHttpResponse all(MyHttpRequest httpRequest, Map<DynamicParam, String> requestParam) throws SQLException {
        CategoryService.findAll();
        return null;
    }

    // Get -> /category/{category}
    public static MyHttpResponse getAllProductOfCategory(MyHttpRequest httpRequest, Map<DynamicParam, String> requestParam) {

        // ID
        String categoryId = requestParam.get(DynamicParam.CATEGORY_ID);

        if(categoryId == null  && categoryId.isBlank()){
            // Erreur 400
            throw new RuntimeException("request param is null or empty");
        }

        List<Product> products =  CategoryService.findProductsByCategoryId(categoryId);

        // TODO : Parser json.
        StringBuilder sb  = new StringBuilder();
        sb.append('[');
        for(Product product : products){
            try {
               String jsonObject = product.toJsonObject(product);
               sb.append(jsonObject).append(',');
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(']');

        System.out.println("json : " + sb);
        return null;
    }
}
