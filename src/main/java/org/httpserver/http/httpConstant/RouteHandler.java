package org.httpserver.http.httpConstant;

import org.httpserver.http.request.MyHttpRequest;
import org.httpserver.http.response.MyHttpResponse;
import org.httpserver.shop.controller.CategoryController;

import java.sql.SQLException;
import java.util.Map;
import java.util.function.BiFunction;

public enum RouteHandler {
    LIST_CATEGORY((httpRequest, dynamicParamStringMap) -> {
        try {
            return CategoryController.getCategory(httpRequest, dynamicParamStringMap);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }),
    LIST_ALL_CATEGORIES((httpRequest, requestParam) -> {
        try {
            return CategoryController.all(httpRequest, requestParam);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }),
    LIST_ALL_PRODUCTS_OF_CATEGORY_ID(CategoryController::getAllProductOfCategory);


    final BiFunction<MyHttpRequest, Map<DynamicParam, String>, MyHttpResponse> customHandler;

    RouteHandler(BiFunction<MyHttpRequest, Map<DynamicParam, String>, MyHttpResponse> customHandler)
    {
        this.customHandler = customHandler;
    }

    public BiFunction<MyHttpRequest, Map<DynamicParam, String>, MyHttpResponse>getHandlerMethod(){
        return this.customHandler;
    }
 }
