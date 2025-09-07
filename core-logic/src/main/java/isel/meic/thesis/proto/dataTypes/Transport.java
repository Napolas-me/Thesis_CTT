package isel.meic.thesis.proto.dataTypes;


/**
 * TODO: COMENTS
 */
public class Transport {
    private final int id;
    private final String name;
    private final String type;
    private int capacity;
    private int maxCapacity;
    private String status;
    private double emissions;

    public Transport(int id, String nome, String type, int capacidadeAtual, int max, String status, double emissions) {
        this.id = id;
        this.name = nome;
        this.type = type;
        this.capacity = capacidadeAtual;
        this.maxCapacity = max;
        this.status = status;
        this.emissions = emissions;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setMaxCapacity(int max) {
        if (max < capacity) {
            this.maxCapacity = max;
        } else {
            throw new IllegalArgumentException("New max capacity must be less than current capacity.");
        }
    }

    public boolean canAdd(int carga) {
        return capacity + carga <= maxCapacity;
    }

    public void add(int carga) {
        capacity += carga;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void resetCapacity() {
        maxCapacity = 0;
    }

    public void setCapacity(int c) {
        this.capacity = c;
    }

    public String getStatus() {
        return status;
    }

    public double getEmissions(){
        return emissions;
    }

    public String getType() {
        return type;
    }
}
