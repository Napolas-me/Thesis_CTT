package isel.meic.thesis.proto.dataTypes;

import java.util.Date;

public class Stop {
    private final int ID;
    private final String local;
    private String gate;
    private Date tempoChegada;
    private Date tempoPartida;
    private String status;//not used yet, but could be useful for future extensions

    public Stop(int id, String local, String gate, Date tempoChegada, Date tempoPartida, String status) {
        this.ID = id;
        this.local = local;
        this.gate = gate;
        this.tempoChegada = tempoChegada;
        this.tempoPartida = tempoPartida;
        this.status = status;
    }

    public int getID() {
        return ID;
    }

    public String getLocal() {
        return local;
    }

    public String getGate() {
        return gate;
    }


    public Date getTempoPartida() {
        return tempoPartida;
    }

    public void setTempoPartida(Date tempoPartida) {
        this.tempoPartida = tempoPartida;
    }

    public Date getTempoChegada() {
        return tempoChegada;
    }

    public void setTempoChegada(Date tempoChegada) {
        this.tempoChegada = tempoChegada;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Paragem{" +
                "local='" + local + '\'' +
                ", gate='" + gate + '\'' +
                ", tempoChegada=" + tempoChegada +
                ", tempoPartida=" + tempoPartida +
                '}';
    }



}
