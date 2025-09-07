package isel.meic.thesis.proto.dataTypes;

import isel.meic.thesis.proto.dataTypes.enums.Type;
import isel.meic.thesis.proto.orq_global.UserParam;

import java.util.Date;

public class Entity {

    protected UserParam userParam;

        public Entity(String ori, String des, Type t, int maxTransfers, Date data) {
            userParam = new UserParam(ori, des, maxTransfers, data, t);
        }

    public String getOrigin() {
        return userParam.getOrigin();
    }

    public String getDestination() {
        return userParam.getDestination();
    }

    public Type getPrioridade() {
        return userParam.getType();
    }

    public int getMaxTransfers() {
        return userParam.getMaxTransfers();
    }

    public Date getDeadLine() {
        return userParam.getDeadline();
    }

    public UserParam getUserParam() {
        return userParam;
    }



    /**
     * TODO: adicionar logica que permita as UAs aceitarem ou nao a rota forneceida pelo orquestrador
     * @param r
     */
    public void getRotaFromOrq(Route r){
        //System.out.println("Got Rota " + r.getName());
        System.out.println(r);

    }

    @Override
    public String toString() {
        return "UAT{" +
                " origem='" + userParam.getOrigin() + '\'' +
                ", destino='" + userParam.getDestination() + '\'' +
                ", prioridade=" + userParam.getType() +
                ", deadline=" + userParam.getDeadline() +
                '}';
    }
}
