package org.httpserver.http.request;

import java.util.Map;

public class MyHttpRequest {
    private final String method;
    private final String path;
    private final String version;
    private final Map<String, String> headers;
    private final String body;

    public MyHttpRequest(String method, String path, String version, Map<String, String> headers, String body){
        this.method  = method;
        this.path    = path;
        this.version = version;
        this.headers = headers;
        this.body = body;
    }

    // ______________ Getters ___________
    public String getMethod(){return this.method;}
    public String getPath(){return this.path;}
    public String getVersion(){return this.version;}
    public Map<String, String> getHeaders(){return this.headers;}
    public String getBody(){return this.body;}
}
