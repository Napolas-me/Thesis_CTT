package isel.meic.thesis.proto.dataTypes.metrics;

import isel.meic.thesis.proto.dataTypes.Route;

import java.util.ArrayList;
import java.util.List;

public class Transfers implements Metric {
    @Override
    public List<Route> sort(List<Route> routes) {
        List<Route> sortedRoutes = new ArrayList<>(routes);
        sortedRoutes.sort((r1, r2) -> {
            int transbordosR1 = r1.getConnections().size();
            int transbordosR2 = r2.getConnections().size();
            return Integer.compare(transbordosR1, transbordosR2);
        });
        return sortedRoutes;
    }
}
