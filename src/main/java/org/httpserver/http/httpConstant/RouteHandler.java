package org.httpserver.http.httpConstant;

import org.httpserver.deprecated.handlerDeprecated.HandlerRegister;
import org.httpserver.http.request.MyHttpRequest;
import org.httpserver.http.response.MyHttpResponse;
import org.httpserver.shop.controller.CategoryController;

import java.util.Map;
import java.util.function.BiFunction;

public enum RouteHandler {
    LIST_ALL_CATEGORIES(CategoryController::all),
    LIST_ALL_BY_CATEGORY_NAME((httpRequest, requestParam) -> CategoryController.getAllFromCategory(httpRequest, requestParam));


    BiFunction<MyHttpRequest, Map<String, String>, MyHttpResponse> customHandler;

    RouteHandler(BiFunction<MyHttpRequest, Map<String, String>, MyHttpResponse> customHandler)
    {
        this.customHandler = customHandler;
    }

    public BiFunction<MyHttpRequest, Map<String, String>, MyHttpResponse>getHandlerMethod(){
        return this.customHandler;
    }
 }
