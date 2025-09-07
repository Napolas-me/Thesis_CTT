package isel.meic.thesis.proto.dataTypes.metrics;

import isel.meic.thesis.proto.dataTypes.Route;

import java.util.LinkedList;
import java.util.List;

public class Cost implements Metric{
    @Override
    public List<Route> sort(List<Route> routes) {
        List<Route> sorted = new LinkedList<>(routes);
        sorted.sort((r1,r2) ->{
                    double em1 = r1.getTransport().getEmissions();
                    double em2 = r2.getTransport().getEmissions();

                    return Double.compare(em1, em2);
        });
        return sorted;
    }
}
