package isel.meic.thesis.core;

import isel.meic.thesis.Device;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//maybe todo: segregate the register as a service

public class Register {

    private Map<UUID, Device> devices = new HashMap<>();

    public Register() {
        System.out.println("Register created");
    }

    public void addDevice(Device device) {
        devices.put(UUID.randomUUID(), device);
    }

    public void removeDevice(UUID id) {
        devices.remove(id);
    }

    public Device getDevice(UUID id) {
        return devices.get(id);
    }

    public Map<UUID,Device> getDevices() {
        return devices;
    }
}
