package isel.meic.thesis.proto.dataTypes.metrics;

import isel.meic.thesis.proto.dataTypes.Route;

import java.util.LinkedList;
import java.util.List;

public class Earliest implements Metric{

    @Override
    public List<Route> sort(List<Route> routes) {
        List<Route> sortedRoutes = new LinkedList<>(routes);
        sortedRoutes.sort((r1, r2) -> {
            long tempoR1 = r1.getArrivalDate().getTime();
            long tempoR2 = r2.getArrivalDate().getTime();
            return Long.compare(tempoR1, tempoR2);
        });
        return sortedRoutes;
    }
}
