package org.httpserver.http.response;

import org.httpserver.http.httpConstant.Status;

import java.util.Map;

public class MyHttpResponse {
    Status status;
    Map<String, String> headers;
    String body;


    public MyHttpResponse(Status status, Map<String, String> headers, String body){
        this.status  = status;
        this.headers = headers;
        this.body    = body;
    }


    public String getBody(){
        return this.body;
    }
}
