package isel.meic.thesis.proto.dataTypes.metrics;

import isel.meic.thesis.proto.dataTypes.Route;

import java.util.LinkedList;
import java.util.List;

/**
 * Metric that sorts routes based on the time it takes to get from point A to point B with the least amount of time.
 */
public class Fastest implements Metric {

    @Override
    public List<Route> sort(List<Route> routes) {
        List<Route> sortedRoutes =new LinkedList<> (routes);
        routes.sort((r1, r2) -> {
            long tempoR1 = r1.getConnectionTime();
            long tempoR2 = r2.getConnectionTime();
            return Long.compare(tempoR1, tempoR2);
        });
        return sortedRoutes;
    }
}
