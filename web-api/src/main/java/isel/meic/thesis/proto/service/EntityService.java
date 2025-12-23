package isel.meic.thesis.proto.service;

import isel.meic.thesis.proto.dao.EntityDAO;
import isel.meic.thesis.proto.dataTypes.Entity;
import isel.meic.thesis.proto.dataTypes.Route; // Importar a classe Route do seu core-logic
import isel.meic.thesis.proto.dataTypes.Stop;
import isel.meic.thesis.proto.dataTypes.Trip;
import isel.meic.thesis.proto.dataTypes.enums.Type; // Importar o enum Type
import isel.meic.thesis.proto.dto.EntityDTO;
import isel.meic.thesis.proto.dto.RouteDTO; // Para retornar a rota selecionada como DTO
import isel.meic.thesis.proto.dto.StopDTO;
import isel.meic.thesis.proto.dto.TripDTO;
import isel.meic.thesis.proto.orq_global.Orquestrador; // Importar o Orquestrador

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class EntityService {

    private final EntityDAO entityDAO;
    private final Orquestrador orquestrador; // Injetar o Orquestrador

    public EntityService(EntityDAO entityDAO, Orquestrador orquestrador) {
        this.entityDAO = entityDAO;
        this.orquestrador = orquestrador;
    }

    /**
     * Cria uma nova entidade UA.
     * @param entity O EntityDTO a ser criado.
     * @return O EntityDTO com o ID gerado pelo banco de dados.
     */
    public EntityDTO createEntity(EntityDTO entity) {
        return entityDAO.createEntity(entity);
    }

    /**
     * Obtém uma entidade UA pelo seu ID.
     * @param id O ID da entidade.
     * @return O EntityDTO correspondente.
     * @throws NoSuchElementException se a entidade não for encontrada.
     */
    public EntityDTO getEntityById(Integer id) {
        EntityDTO entity = entityDAO.findById(id);
        if (entity == null) {
            throw new NoSuchElementException("Entidade com ID " + id + " não encontrada.");
        }
        return entity;
    }

    /**
     * Obtém todas as entidades UA.
     * @return Uma lista de EntityDTOs.
     */
    public List<EntityDTO> getAllEntities() {
        return entityDAO.findAll();
    }

    public EntityDTO updateEntityStatus(int id, String status){
        EntityDTO e = getEntityById(id);
        e.setStatus(status);
        int rows = entityDAO.updateEntity(e);
        return getEntityById(id);//bs code need to streamline this
    }

    /**
     * Atualiza uma entidade UA existente com um id de uma rota.
     * @param routeId O ID de uma rota a ser adicionado a uma entidade.
     * @param entity O EntityDTO com os dados atualizados.
     * @return O EntityDTO atualizado.
     * @throws NoSuchElementException se a entidade não for encontrada.
     */
    public EntityDTO updateEntityRoute(Integer routeId, EntityDTO entity) {
        if (entity == null) {
            throw new NoSuchElementException("Entidade Vazia");
        }
        System.out.println("Update routeId is " + routeId );
        entity.setRouteId(routeId);
        int rowsAffected = entityDAO.updateEntity(entity);
        System.out.println(rowsAffected);
        if (rowsAffected == 0) {
            throw new RuntimeException("Falha ao atualizar a entidade com ID " + entity.getRouteId());
        }
        return entity;
    }

    /**
     * Elimina uma entidade UA pelo seu ID.
     * @param id O ID da entidade a ser eliminada.
     * @throws NoSuchElementException se a entidade não for encontrada.
     */
    public void deleteEntity(Integer id) {
        int rowsAffected = entityDAO.deleteEntity(id);
        if (rowsAffected == 0) {
            throw new NoSuchElementException("Entidade com ID " + id + " não encontrada para eliminação.");
        }
    }

    /**
     * Obtém todas as entidades UA com status 'active'.
     * Mapeia para o endpoint /api/entities/getactive.
     * @return Uma lista de EntityDTOs com status 'active'.
     */
    public List<EntityDTO> getActiveEntities() {
        return entityDAO.findByStatus("active");
    }

    public void resetActive(){
        getActiveEntities()
                .forEach(a ->{
                    a.setStatus("created");
                    updateEntityRoute(0, a);
                });
    }

    /**
     * Calcula a rota ótima para uma entidade UA específica, utilizando o Orquestrador.
     * Mapeia para o endpoint /api/entities/get-route.
     * @param entityId O ID da entidade UA para a qual calcular a rota.
     * @return O RouteDTO da rota ótima selecionada, ou null se nenhuma rota for encontrada.
     * @throws NoSuchElementException se a entidade com o ID fornecido não for encontrada.
     */
    public RouteDTO calculateOptimalRouteForEntity(Integer entityId) {
        // 1. Obter a EntityDTO da base de dados (do web-api)
        EntityDTO entityDTO = getEntityById(entityId); // Reutiliza o método existente que lança NoSuchElementException

        // 2. Mapear EntityDTO (do web-api) para Entity (do core-logic)
        // Assumindo que o construtor de Entity no core-logic corresponde aos campos do EntityDTO
        // ou que Entity tem setters para estes campos.
        Entity entityCoreLogic = new Entity(
                entityDTO.getOrigin(),
                entityDTO.getDestination(),
                Type.valueOf(Objects.requireNonNullElse(entityDTO.getType(), "NORMAL").toUpperCase()), // Converte String para Enum
                Objects.requireNonNullElse(entityDTO.getMaxTransfers(), 0),
                convertToDate(entityDTO.getDeadline())
        );

        // 3. Chamar o método process do Orquestrador com a Entity do core-logic
        Route selectedRoute = orquestrador.process(entityCoreLogic);

        // 4. Mapear o objeto Route (do core-logic) para RouteDTO (da API)
        if (selectedRoute != null) {
            return mapRouteToDTO(selectedRoute);
        }
        return null;
    }

    // Helper method to map Route (core-logic) to RouteDTO (API)
    private RouteDTO mapRouteToDTO(Route route) {
        RouteDTO dto = new RouteDTO();
        dto.setId(route.getId());
        dto.setName(route.getName());
        dto.setRouteStartName(route.getOrigin());
        dto.setRouteEndName(route.getDestination());
        dto.setRouteStartDate(convertToLocalDateTime(route.getDepartureDate()));
        dto.setRouteEndDate(convertToLocalDateTime(route.getArrivalDate()));
        dto.setStatus(route.getStatus());

        if (route.getTransport() != null) {
            dto.setAssignedTransport(mapTransportToDTO(route.getTransport()));
        }

        if (route.getConnections() != null && !route.getConnections().isEmpty()) {
            dto.setSequence(mapConnectiontoAPI(route.getConnections()));
        }
        return dto;
    }

    private List<Object> mapConnectiontoAPI(List<Object> con){
        List<Object> mappedConnections = new ArrayList<>();

        con.forEach(c ->{
            if(c.getClass().getName().contains("Trip")){
                TripDTO t = tripToTripDTO((Trip) c);
                mappedConnections.add(t);
            }
            else if(c.getClass().getName().contains("Stop")){
                StopDTO s = stopToStopDTO((Stop) c);
                mappedConnections.add(s);
            }
        });
        //System.out.println(mappedConnections);
        return mappedConnections;
    }

    private TripDTO tripToTripDTO(Trip t){
        TripDTO returnTrip = new TripDTO();

        returnTrip.setId(t.getID());
        returnTrip.setOrigin(t.getOrigem());
        returnTrip.setDestination(t.getDestino());
        returnTrip.setDepartureDate(convertToLocalDateTime(t.getTempoPartida()));
        returnTrip.setDestinationDate(convertToLocalDateTime(t.getTempoChegada()));
        returnTrip.setStatus(t.getStatus());
        return returnTrip;
    }

    private StopDTO stopToStopDTO(Stop s){
        StopDTO returnStop = new StopDTO();

        returnStop.setId(s.getID());
        returnStop.setStopName(s.getLocal());
        returnStop.setGateName(s.getGate());
        returnStop.setArrivalDate(convertToLocalDateTime(s.getTempoChegada()));
        returnStop.setDepartureDate(convertToLocalDateTime(s.getTempoPartida()));
        returnStop.setStatus(s.getStatus());

        return returnStop;
    }

    // Helper method to map Transport (core-logic) to TransportDTO (API)
    private isel.meic.thesis.proto.dto.TransportDTO mapTransportToDTO(isel.meic.thesis.proto.dataTypes.Transport transport) {
        isel.meic.thesis.proto.dto.TransportDTO dto = new isel.meic.thesis.proto.dto.TransportDTO();
        dto.setId(transport.getId());
        dto.setName(transport.getName());
        dto.setType(transport.getType().toString()); // Converte Type ENUM para String
        dto.setCapacity(transport.getCapacity());
        dto.setMaxCapacity(transport.getMaxCapacity());
        dto.setStatus(transport.getStatus().toString()); // Converte Status ENUM para String
        dto.setCarbonEmissionsGkm(transport.getEmissions());
        return dto;
    }

    /**
     * Converte java.util.Date para LocalDateTime.
     * Assume o fuso horário padrão do sistema para a conversão.
     * Esta função é adicionada localmente no EntityService.
     * @param date O objeto java.util.Date a ser convertido.
     * @return O objeto LocalDateTime correspondente.
     */
    private LocalDateTime convertToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Converte LocalDateTime para java.util.Date.
     * Assume o fuso horário padrão do sistema para a conversão.
     * Esta função é adicionada localmente no EntityService.
     * @param localDateTime O objeto LocalDateTime a ser convertido.
     * @return O objeto java.util.Date correspondente.
     */
    private Date convertToDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Função de depuração: Redefine o 'deadline' de todas as entidades UA para a data e hora atuais.
     * @return O número de linhas afetadas.
     */
    public int resetAllEntityDeadlines() {
        return entityDAO.resetAllDeadlinesToNow();
    }

    /**
     * Função de depuração: Redefine o 'deadline' de todas as entidades UA para 23:55 da data atual.
     * @return O número de linhas afetadas.
     */
    public int resetAllDeadlinesToLastSlot() {
        return entityDAO.resetAllDeadlinesToLastSlot();
    }
}
