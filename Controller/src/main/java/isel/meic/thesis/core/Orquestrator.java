package isel.meic.thesis.core;

import java.util.*;

public class Orquestrator { //todo: should be a service

    private Map<UUID, Process> processes = new HashMap<>();
    private List<Item> items = new ArrayList<>();

    public Orquestrator() {
        System.out.println("Orquestrator created");
    }

    public void addProcess(String name, String description) {
        Process process = new Process(name, description);
        processes.put(process.getId(), process);
    }

    public void removeProcess(UUID id) {
        processes.remove(id);
    }

    public void runProcess(UUID id) {
        processes.get(id).run();
    }

    public void addNewItem(String name, String description) {
        Item item = new Item(name, description);
        items.add(item);
    }


}
