package org.httpserver.http.httpHelper;

import org.httpserver.http.httpConstant.DynamicParam;
import org.httpserver.http.routeur.Route;

import java.util.Map;

public record HelperFormHandler(Map<DynamicParam, String> requestParam, Route route){}
