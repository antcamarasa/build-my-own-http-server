import org.httpserver.http.httpConstant.deprecated.Handler;
import org.httpserver.http.httpConstant.SegmentStatus;
import org.httpserver.http.httpConstant.Verb;
import org.httpserver.http.routeur.Route;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestMotifInRoute {
    @Test
    public void testMotifInRouteCreation(){
        Route route = new Route(Verb.GET, "/categories", Handler.NON_USING);
        Assertions.assertEquals(SegmentStatus.FIXE, route.getMotif()[0].segmentStatus());
        Assertions.assertEquals("", route.getMotif()[0].value());

        Assertions.assertEquals(SegmentStatus.FIXE, route.getMotif()[1].segmentStatus());
        Assertions.assertEquals("categories", route.getMotif()[1].value());
    }

    @Test
    public void testMotifInRouteCreationWithDynamic(){
        Route route = new Route(Verb.GET, "/categories/{category}", Handler.NON_USING);
        Assertions.assertEquals(SegmentStatus.FIXE, route.getMotif()[0].segmentStatus());
        Assertions.assertEquals("", route.getMotif()[0].value());

        Assertions.assertEquals(SegmentStatus.FIXE, route.getMotif()[1].segmentStatus());
        Assertions.assertEquals("categories", route.getMotif()[1].value());

        Assertions.assertEquals(SegmentStatus.DYNAMIC, route.getMotif()[2].segmentStatus());
        Assertions.assertEquals("category", route.getMotif()[2].value());
    }
}
