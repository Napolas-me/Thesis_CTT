package isel.meic.thesis.proto.orq_global;

import isel.meic.thesis.proto.dataTypes.Entity;
import isel.meic.thesis.proto.dataTypes.metrics.*;
import isel.meic.thesis.proto.dataTypes.enums.Type;

public class DefaultMetricSelectionStrategy implements MetricSelectionStrategy {
    @Override
    public Metric selectMetric(Entity entity) {
        if (entity.getPrioridade() == Type.AZUL)
            return new Fastest();
        else if(entity.getPrioridade() == Type.EXPRESSO)
            return new Earliest();
        else if(entity.getPrioridade() == Type.VERDE)
            return new Cost();
        else if (entity.getMaxTransfers() != -1)
            return new Transfers();
        else
            return new Cost(); // Caso padrão, sempre o mais economico
    }
}