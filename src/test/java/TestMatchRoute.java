import org.httpserver.http.MyHttpServer;
import org.httpserver.http.httpConstant.deprecated.Handler;
import org.httpserver.http.httpConstant.Verb;
import org.httpserver.http.request.MyHttpRequest;
import org.httpserver.http.routeur.Route;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

public class TestMatchRoute {
    MyHttpServer myHttpServer;
    List<Route> routes = List.of(
            new Route(Verb.GET, "/categories", Handler.LIST_ALL_CATEGORIES), // Route stocke une valeur d'enum.
            new Route(Verb.GET, "/categories/{category}", Handler.LIST_ALL_CATEGORIES), // TODO modifier le handler
            new Route(Verb.POST, "/categories", Handler.LIST_ALL_CATEGORIES)
    );

    @BeforeEach
    public void setUp(){
        this.myHttpServer = new MyHttpServer(8080);
    }


    /**
    @Test
    public void testFindRouteCustomParam2(){
        MyHttpRequest httpRequest = new MyHttpRequest(
                "GET", "/categories/VEGETABLES", "HTTP/1.1", new HashMap<>(), ""
        );
        Route route = myHttpServer.findRoute(httpRequest, routes);
        Assertions.assertEquals(Verb.GET, route.getVerb());
        Assertions.assertEquals("/categories/{category}", route.getPath());
    }

    @Test
    public void testFindRouteDirect(){
        MyHttpRequest httpRequest = new MyHttpRequest(
                "GET", "/categories", "HTTP/1.1", new HashMap<>(), ""
        );
        Route route = myHttpServer.findRoute(httpRequest, routes);
        Assertions.assertEquals(Verb.GET, route.getVerb());
        Assertions.assertEquals("/categories", route.getPath());
    }

    @Test
    public void testFindRouteNull(){
        MyHttpRequest httpRequest = new MyHttpRequest(
                "GET", "/categorie", "HTTP/1.1", new HashMap<>(), ""
        );
        Route route = myHttpServer.findRoute(httpRequest, routes);
        Assertions.assertNull(route);
    }

    @Test
    public void testMatchRoute(){
        MyHttpRequest httpRequest = new MyHttpRequest(
                "GET", "/categories/FRUITS", "HTTP/1.1", new HashMap<>(), ""
        );

        Route foundedRoute = myHttpServer.findRouteStream(httpRequest, routes);
        Assertions.assertEquals(Verb.GET, foundedRoute.getVerb());
        Assertions.assertEquals("/categories/{category}", foundedRoute.getPath());
    }
    */
}
