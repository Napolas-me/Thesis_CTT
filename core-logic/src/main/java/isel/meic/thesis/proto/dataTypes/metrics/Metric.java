package isel.meic.thesis.proto.dataTypes.metrics;

import isel.meic.thesis.proto.dataTypes.Route;
import java.util.List;

public interface Metric {
    public List<Route> sort(List<Route> routes);
}
