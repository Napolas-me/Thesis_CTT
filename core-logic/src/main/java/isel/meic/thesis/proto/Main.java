package isel.meic.thesis.proto;

import isel.meic.thesis.proto.dataTypes.*;
import isel.meic.thesis.proto.dataTypes.enums.Type;
import isel.meic.thesis.proto.orq_global.Orquestrador;

import java.util.*;

public class Main {
    private static Orquestrador orquestrador;
    private static Map<Integer, Entity> entities = new HashMap<>();

    /**
     * Método auxiliar para criar uma data com hora e minuto específicos.
     * @param hour Hora a ser definida (0-23).
     * @param minute Minuto a ser definido (0-59).
     * @return Objeto Date com a hora e minuto especificados.
     */
    public static Date createDate(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar.getTime();
    }
    public static void Menu(){
        int state = 0;
        boolean exit = false;
        while(!exit) {
            Scanner scanner = new Scanner(System.in);

            switch (state) {
                case 0:
                    System.out.println("----------Menu----------");
                    System.out.println("1. Create Cassete");
                    System.out.println("2. Process Cassete");
                    System.out.println("3. Modify Cassetes");
                    System.out.println("4. List Cassetes");
                    System.out.println("5. Exit");
                    System.out.println("------------------------");
                    //Ler a opção do utilizador
                    state = scanner.nextInt();
                    break;
                case 1:
                    // Lógica para criar uma cassete
                    System.out.println("Creating a new cassete...");
                    // Ler os dados da cassete
                    System.out.print("Enter origin:");
                    String origin = scanner.next();
                    System.out.print("Enter destination:");
                    String destination = scanner.next();
                    System.out.print("Enter priority (1-EXPRESSO, 2-VERDE, 3-AZUL, 4-NORMAL):");
                    int priorityInput = scanner.nextInt();
                    Type priority = Type.fromInt(priorityInput);
                    System.out.print("Enter max transfers (-1 for no limit):");
                    int maxTransfers = scanner.nextInt();
                    System.out.println("Enter deadline (hour and minute):");
                    System.out.print("Hour (0-23): ");
                    int hour = scanner.nextInt();
                    System.out.print("Minute (0-59): ");
                    int minute = scanner.nextInt();
                    Date deadline = createDate(hour, minute);
                    // Criar a nova cassete
                    Entity newEntity = new Entity(origin, destination, priority, maxTransfers, deadline);
                    // Adicionar a cassete ao mapa de cassetes
                    int newId = entities.size() + 1; // Generate a new ID
                    entities.put(newId, newEntity);
                    System.out.println("Cassete created with ID: " + newId);
                    state = 0; // Reset state to show menu again
                    break;
                case 2:
                    // Lógica para processar uma cassete
                    System.out.println("Processing cassetes...");
                    //get id of the cassete to process
                    System.out.print("Enter the ID of the cassete to process:");
                    int casseteId = scanner.nextInt();
                    Entity entityToProcess = entities.get(casseteId);
                    if (entityToProcess != null) {
                        System.out.println("Processing " + entityToProcess);
                        Route route = orquestrador.process(entityToProcess);
                        if (route != null) {
                            entityToProcess.getRotaFromOrq(route);
                            orquestrador.acceptRoute(route); // Accept the route, todo: in the future, this is a decision made by the user aka the object being processed
                        } else {
                            System.out.println("No route found for the given entity.");
                        }
                    } else {
                        System.out.println("Cassete not found with ID: " + casseteId);
                    }
                    state = 0; // Reset state to show menu again
                    break;
                case 3:
                    // Lógica para modificar cassetes
                    System.out.println("Modifying cassetes...");
                    //get id of the cassete to modify
                    System.out.print("Enter the ID of the cassete to modify:");
                    int modifyId = scanner.nextInt();
                    Entity entityToModify = entities.get(modifyId);
                    if (entityToModify != null) {
                        boolean modify = true;
                        while (modify) {
                            System.out.println("Current details: " + entityToModify);
                            // Ler novos dados da cassete
                            //utilizador escolhe qual o parametro a modificar de uma lista
                            System.out.println("Choose a parameter to modify:");
                            System.out.println("1. Origin");
                            System.out.println("2. Destination");
                            System.out.println("3. Priority (1-EXPRESSO, 2-VERDE, 3-AZUL, 4-NORMAL)");
                            System.out.println("4. Max Transfers (-1 for no limit)");
                            System.out.println("5. Deadline (hour and minute)");
                            System.out.println("6. Exit");
                            System.out.print("Enter your choice (1-6): ");

                            int modifyOption = scanner.nextInt();
                            switch (modifyOption) {
                                case 1:
                                    System.out.print("Enter new origin: ");
                                    String newOrigin = scanner.next();
                                    entityToModify.getUserParam().setOrigin(newOrigin);
                                    break;
                                case 2:
                                    System.out.print("Enter new destination: ");
                                    String newDestination = scanner.next();
                                    entityToModify.getUserParam().setDestination(newDestination);
                                    break;
                                case 3:
                                    System.out.print("Enter new priority (1-EXPRESSO, 2-VERDE, 3-AZUL, 4-NORMAL): ");
                                    int newPriorityInput = scanner.nextInt();
                                    Type newPriority = Type.fromInt(newPriorityInput);
                                    entityToModify.getUserParam().setType(newPriority);
                                    break;
                                case 4:
                                    System.out.print("Enter new max transfers (-1 for no limit): ");
                                    int newMaxTransfers = scanner.nextInt();
                                    entityToModify.getUserParam().setMaxTransfers(newMaxTransfers);
                                    break;
                                case 5:
                                    System.out.println("Enter new deadline (hour and minute):");
                                    System.out.print("Hour (0-23): ");
                                    int newHour = scanner.nextInt();
                                    System.out.print("Minute (0-59): ");
                                    int newMinute = scanner.nextInt();
                                    Date newDeadline = createDate(newHour, newMinute);
                                    entityToModify.getUserParam().setDeadline(newDeadline);
                                    break;
                                case 6:
                                    System.out.println("Exiting modification menu.");
                                    modify = false; // Exit modification loop
                                    break;
                                default:
                                    System.out.println("Invalid option. No changes made.");
                            }
                        }
                    } else {
                        System.out.println("Cassete not found with ID: " + modifyId);
                    }
                    state = 0; // Reset state to show menu again
                    break;
                case 4:
                    // Lógica para listar as cassetes
                    System.out.println("Showing cassetes...");
                    entities.forEach((key, entity) -> {
                        System.out.println("Cassete ID: " + key + ", " + entity);
                    });
                    state = 0; // Reset state to show menu again
                    break;
                case 5:
                    System.out.println("Saindo...");
                    //close the scanner
                    scanner.close();
                    //close the connection to the database
                    orquestrador.closeConnectionDB();
                    exit = true;
                    break;
                default:
                    System.out.println("Opção inválida, tente novamente.");
                    state = 0;
                    break;
            }
        }
    }


    public static void main(String[] args) {
        orquestrador = new Orquestrador();

        // Criar uma cassete com destino final Faro
        Entity entity1 = new Entity("Porto", "Faro", Type.EXPRESSO, 0, createDate(18, 0));
        Entity entity2 = new Entity("Porto", "Faro", Type.VERDE, 0, createDate(18, 0));
        Entity entity3 = new Entity("Porto", "Faro", Type.AZUL, 0, createDate(18, 0));
        //Entity entity4 = new Entity("Porto", "Faro", Type.NORMAL, 0, createDate(18, 0));

        //Entity entity5 = new Entity("Porto", "Faro", Type.NORMAL, 0, createDate(18, 0)); // Max 0 transfers (should pick R1)
        //Entity entity6 = new Entity("Porto", "Faro", Type.NORMAL, 1, createDate(18, 0)); // Max 1 transfer (should pick R2/R3 based on time/cost)
        //Entity entity7 = new Entity("Lisboa", "Porto", Type.NORMAL, 2, createDate(18, 0)); // Max 2 transfers (should pick R9)
        //Entity entity8 = new Entity("Lisboa", "Porto", Type.NORMAL, 0, createDate(18, 0)); // Max 0 transfers (should pick R7)

        // Scenario 2: Testing VERDE (Cost/Emissions) for Porto -> Lisboa
        // Existing: R4, R5, R6 (all cars). New: R14 (motorcycle)
        //Entity entity9 = new Entity("Porto", "Lisboa", Type.VERDE, 0, createDate(18, 0)); // Should prefer R14 (motorcycle)

        // Scenario 3: Testing AZUL (Fastest) for Lisboa -> Faro
        // Routes: R10, R11, R12
        //Entity entity10 = new Entity("Lisboa", "Faro", Type.AZUL, 0, createDate(18, 0)); // Should pick the fastest among R10, R11, R12

        // Scenario 4: Testing EXPRESSO (Earliest) for Coimbra -> Faro
        // Route: R13
        //Entity entity11 = new Entity("Coimbra", "Faro", Type.EXPRESSO, 0, createDate(18, 0)); // Should pick R13

        // Scenario 5: Testing with tight deadlines
        //Entity entity12 = new Entity("Porto", "Faro", Type.NORMAL, 0, createDate(10, 0)); // Deadline before any route ends (might return empty or specific error)
        //Entity entity13 = new Entity("Porto", "Faro", Type.NORMAL, 0, createDate(10, 45)); // Deadline for R1 (ends 10:30)


        entities.put(1, entity1);
        entities.put(2, entity2);
        entities.put(3, entity3);


        Menu();
    }
}