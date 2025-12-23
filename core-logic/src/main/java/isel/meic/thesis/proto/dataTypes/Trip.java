package isel.meic.thesis.proto.dataTypes;

import java.sql.Timestamp;
import java.util.Date;

public class Trip {
    private final int ID;
    private final String origem;
    private final String destino;
    private Date tempoPartida;
    private Date tempoChegado;
    private String status; // not used yet, but could be useful for future extensions

    public Trip(int id, String origem, String destino, Date tempoPartida, Date tempoChegado) {
        ID = id;
        this.origem = origem;
        this.destino = destino;
        this.tempoPartida = tempoPartida;
        this.tempoChegado = tempoChegado;
    }

    public Trip(int id, String origem, String destino, Timestamp tempoPartida, Timestamp tempoChegada, String status) {
        this.ID = id;
        this.origem = origem;
        this.destino = destino;
        this.tempoPartida = new Date(tempoPartida.getTime());
        this.tempoChegado = new Date(tempoChegada.getTime());
        this.status = status;
    }

    public int getID() {
        return ID;
    }

    public String getOrigem() {
        return origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setTempoPartida(Date tempoPartida) {
        this.tempoPartida = tempoPartida;
    }

    public Date getTempoPartida() {
        return tempoPartida;
    }

    public void setTempoChegada(Date tempoChegado) {
        this.tempoChegado = tempoChegado;
    }

    public Date getTempoChegada() {
        return tempoChegado;
    }

    public String getStatus(){return status;}

    @Override
    public String toString() {
        return "Viagem{" +
                "origem='" + origem +
                ", destino='" + destino  +
                ", tempoPartida=" + tempoPartida +
                ", tempoChegado=" + tempoChegado +
                '}' ;
    }
}
