package org.httpserver.http.request;
import java.util.Map;

public class MyHttpRequestBuilder {
    String method;
    String path;
    String version;
    Map<String, String> headers;
    String body;

    public MyHttpRequestBuilder withMethod(String method){
        if(method == null){throw new IllegalStateException();}
        this.method = method;
        return this;
    }
    public MyHttpRequestBuilder withPath(String path){
        if(path == null){throw new IllegalStateException();}
        this.path = path;
        return this;
    }
    public MyHttpRequestBuilder withVersion(String version){
        if(version == null){throw new IllegalStateException();}
        this.version = version;
        return this;
    }
    public MyHttpRequestBuilder withHeaders(Map<String, String> headers){
        if(headers == null){throw new IllegalStateException();}
        this.headers = headers;
        return this;
    }
    public MyHttpRequestBuilder withBody(String body){
        if(body == null){throw new IllegalStateException();}
        this.body = body;
        return this;
    }

    public MyHttpRequest build(){
        return new MyHttpRequest(method, path, version, headers, body);
    }


    // Attention ici j'expose => headers.
}
