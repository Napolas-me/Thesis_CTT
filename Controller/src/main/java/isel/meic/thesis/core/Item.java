package isel.meic.thesis.core;

import java.util.UUID;

public class Item {

    private UUID id;
    private String name;
    private String description;

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
        this.id = UUID.randomUUID();
    }
    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
