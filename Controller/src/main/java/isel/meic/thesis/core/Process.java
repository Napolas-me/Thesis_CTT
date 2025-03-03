package isel.meic.thesis.core;

import isel.meic.thesis.Device;

import java.util.UUID;

public class Process {
    private final UUID id;
    private String name;
    private String description;
    private Status status;

    private Item item;
    private Device device;

    public Process(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.Created;
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public void run() {
        status = Status.Running;
        System.out.println("Process " + name + " is running");

        //TODO: implement the process logic to be generic and needs to be linked to the device
    }

    public void stop() {
        status = Status.Stopped;
        System.out.println("Process " + name + " is stopped");
    }

    public Status getStatus() {
        return status;
    }

}
