package org.httpserver.http.httpHelper;

import org.httpserver.http.routeur.Route;

import java.util.Map;

public record HelperFormHandler(Map<String, String> requestParam, Route route){}
