package isel.meic.thesis.proto.dataTypes;

import java.util.*;

public class Route {
    private int id;
    private String name;
    private String origem;
    private String destino;
    private Date tempoPartida;
    private Date tempoChegada;
    private Transport transport;
    private String status;

    private List<Object> ligacoes;

    /**
     * Construtor da classe Rota
     * <p>
     * Cada Rota é identificada por um id único, e tem um nome que é uma combinação
     * da origem e destino.
     * Na construção da Rota, é necessário fornecer a origem, destino, hora de partida,
     * hora de chegada e o transporte associado à rota.
     * A rota é composta por uma lista de paragens ou viagens, que podem ser adicionadas
     * posteriormente.
     *
     * @param tempoPartida tempo de partida inicial. Pode ser atualizado
     * @param tempoChegada tempo de chegada final. Pode ser atualizado
     * @param id identificador da rota
     */
    public Route(int id, String name, String origem, String destino,
                 Date tempoPartida, Date tempoChegada, Transport t, String status) {
        this.id = id;
        this.name = name;
        this.origem = origem;
        this.destino = destino;
        this.tempoPartida = tempoPartida;
        this.tempoChegada = tempoChegada;
        this.status = status;
        this.transport = t;
    }

    public String getName() {
        return name;
    }

    public String getOrigin() {
        return origem;
    }

    public String getDestination() {
        return destino;
    }

    public int getId() {
        return id;
    }

    public Date getDepartureDate() {
        return tempoPartida;
    }

    public Date getArrivalDate() {
        return tempoChegada;
    }

    public List<Object> getConnections() {
        return ligacoes;
    }

    public void setTransport(Transport t){
        this.transport = t;
    }

    public Transport getTransport(){
        return transport;
    }

    //millis
    public long getConnectionTime() {
        return  tempoChegada.getTime() - tempoPartida.getTime();
    }

    public void setLigacoes(List<Object> ligacoes) {
        this.ligacoes = ligacoes;
    }

    public void setStatus(String s){
        this.status = s;
    }

    public String getStatus(){
        return status;
    }


    @Override
    public String toString() {
        return "Rota{" + '\n' +
                "\tid=" + id + '\n' +
                "\tname='" + name + '\n' +
                "\torigem='" + origem + '\n' +
                "\tdestino='" + destino + '\n' +
                "\ttempoPartida=" + tempoPartida + '\n' +
                "\ttempoChegada=" + tempoChegada + '\n' +
                '}';
    }
}

