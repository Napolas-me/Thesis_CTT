package isel.meic.thesis.proto.dataTypes.metrics;

import isel.meic.thesis.proto.dataTypes.Route;

import java.util.List;

public class Capacity implements Metric {

    @Override
    public List<Route> sort(List<Route> routes) {
        List<Route> sortedRoutes = routes;
        //sortedRoutes.sort((r1, r2) -> {
            //int lotacaoR1 = r1.getTransporte().getCapacity();
            //int lotacaoR2 = r2.getTransporte().getCapacity();
            //return Integer.compare(lotacaoR1, lotacaoR2);
       // });

        return sortedRoutes;
    }
}
