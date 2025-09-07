package isel.meic.thesis.proto.orq_global;

import isel.meic.thesis.proto.dataTypes.enums.Type;

import java.util.Date;

public class UserParam {

    private String origin;
    private String destination;
    private int maxTransfers;
    private Date deadline;
    private Type type;

    public UserParam(String origin, String destination, int maxTransfers, Date deadline, Type type) {
        this.origin = origin;
        this.destination = destination;
        this.maxTransfers = maxTransfers;
        this.deadline = deadline;
        this.type = type;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public int getMaxTransfers() {
        return maxTransfers;
    }

    public Date getDeadline() {
        return deadline;
    }

    public Type getType() {
        return type;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setMaxTransfers(int maxTransfers) {
        this.maxTransfers = maxTransfers;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "UserParam{" +
                "origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", maxTransfers=" + maxTransfers +
                ", deadline=" + deadline +
                ", type=" + type +
                '}';
    }
}
