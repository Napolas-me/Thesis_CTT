package isel.meic.thesis.proto.orq_global;

import isel.meic.thesis.proto.dataTypes.Entity;
import isel.meic.thesis.proto.dataTypes.metrics.Metric;

public interface MetricSelectionStrategy {
    Metric selectMetric(Entity entity);
}