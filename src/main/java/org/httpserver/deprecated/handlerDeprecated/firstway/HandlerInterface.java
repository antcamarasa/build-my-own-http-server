package org.httpserver.deprecated.handlerDeprecated.firstway;

import org.httpserver.http.request.MyHttpRequest;
import org.httpserver.http.response.MyHttpResponse;

import java.util.Map;

public interface HandlerInterface {
    MyHttpResponse process(MyHttpRequest httpRequest, Map<String, String> pathParams);
}
