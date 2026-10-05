package org.httpserver.deprecated.handlerDeprecated.firstway;

import org.httpserver.http.httpConstant.Status;
import org.httpserver.http.request.MyHttpRequest;
import org.httpserver.http.response.MyHttpResponse;

import java.util.HashMap;
import java.util.Map;

public class ListAllCategories implements HandlerInterface{

    @Override
    public MyHttpResponse process(MyHttpRequest httpRequest, Map<String, String> pathParam) {
        return new MyHttpResponse(Status.OK, new HashMap<>(), "All categories listed");
    }
}
