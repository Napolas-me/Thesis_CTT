package isel.meic.thesis.proto.warehouse.datatypes;

import isel.meic.thesis.proto.dataTypes.Transport;

public class Gate {

    private String name;
    private String location;
    private Transport transport;
    private boolean isActive;
    private int inRouteCount; // Count of routes passing through this gate


    public Gate(String name, String location, Transport transport) {
        this.name = name;
        this.location = location;
        this.transport = transport;
        this.isActive = false; // Default to active
        this.inRouteCount = 0; // Initialize route count
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Transport getTransport() {
        return transport;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getInRouteCount() {
        return inRouteCount;
    }
    public void incrementInRouteCount() {
        inRouteCount++;
    }
    public void decrementInRouteCount() {
        if (inRouteCount > 0)
            inRouteCount--;
        else
            System.out.println("Error: Cannot decrement inRouteCount below zero.");
    }
}
