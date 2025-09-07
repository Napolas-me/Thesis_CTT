package isel.meic.thesis.proto.warehouse;

import isel.meic.thesis.proto.warehouse.datatypes.Gate;

import java.util.HashMap;
import java.util.Map;

public class WarehouseData {

    private Map<String, Gate> gates;

    public WarehouseData() {
        this.gates = new HashMap<>();
    }

    public void addGate(Gate gate) {
        if (gate != null && gate.getName() != null) {
            gates.put(gate.getName(), gate);
        }
    }

    public Gate getGate(String name) {
        return gates.get(name);
    }

    public int getinRouteCount(String gateName) {
        Gate gate = gates.get(gateName);
        if (gate != null) {
            return gate.getInRouteCount();
        }
        return -1; // Return 0 if gate not found
    }
}
