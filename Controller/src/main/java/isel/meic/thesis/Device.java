package isel.meic.thesis;

public class Device {

    private String name;
    private String id;
    private int type;

    public Device(String name, String id, int type) {
        this.name = name;
        this.id = id;
        this.type = type;
    }

    public boolean status() {
        return true;
    }

    public void sendAction(int action) {

    }

    public void sendMessage(String message) {
    }

    @Override
    public String toString() {
        return "Device{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", type=" + type +
                '}';
    }
}
