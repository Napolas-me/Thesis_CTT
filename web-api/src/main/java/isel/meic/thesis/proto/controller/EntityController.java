package isel.meic.thesis.proto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import isel.meic.thesis.proto.dto.EntityDTO;
import isel.meic.thesis.proto.dto.RouteDTO; // Importar RouteDTO
import isel.meic.thesis.proto.service.EntityService;
import lombok.Data; // Para a classe aninhada de Request
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@RestController
@RequestMapping("/api/entities")
public class EntityController {

    private final EntityService entityService;

    @Autowired // Autowire the ObjectMapper
    private ObjectMapper objectMapper;

    public EntityController(EntityService entityService) {
        this.entityService = entityService;
    }

    // --- Endpoints CRUD existentes (mantidos iguais) ---

    @PostMapping
    public ResponseEntity<EntityDTO> createEntity(@RequestBody EntityDTO entityDTO) {
        EntityDTO createdEntity = entityService.createEntity(entityDTO);
        return new ResponseEntity<>(createdEntity, HttpStatus.CREATED);
    }

    @GetMapping
    public List<EntityDTO> getAllEntities() {
        return entityService.getAllEntities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityDTO> getEntityById(@PathVariable Integer id) {
        try {
            EntityDTO entity = entityService.getEntityById(id);
            return ResponseEntity.ok(entity);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityDTO> updateEntity(@PathVariable Integer id, @RequestBody EntityDTO entityDTO) {
        try {
            EntityDTO updatedEntity = entityService.updateEntity(id, entityDTO);
            return ResponseEntity.ok(updatedEntity);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntity(@PathVariable Integer id) {
        try {
            entityService.deleteEntity(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- Novos Endpoints ---

    /**
     * Endpoint para obter todas as entidades UA com status 'active'.
     * Mapeia para requisições GET em /api/entities/getactive.
     * Corresponde a 'getRunningProcesses()' no seu frontend.
     * @return Uma lista de EntityDTOs com status 'active'.
     */
    @GetMapping("/getactive")
    public List<EntityDTO> getActiveEntities() {
        return entityService.getActiveEntities();
    }

    /**
     * DTO para receber o ID da entidade para cálculo de rota.
     */
    @Data // Lombok annotation
    static class EntityIdRequest {
        private Integer entityId;
    }

    /**
     * Endpoint para calcular a rota ótima para uma entidade UA específica.
     * Mapeia para requisições POST em /api/entities/get-route.
     * Corresponde a 'calculateRoute()' no seu frontend.
     * @param request Um objeto JSON contendo o 'entityId'.
     * @return ResponseEntity com o RouteDTO da rota ótima e status 200 OK,
     * ou 404 Not Found se a entidade ou rota não for encontrada,
     * ou 500 Internal Server Error em caso de falha.
     */
    @PostMapping("/get-route")
    public ResponseEntity<RouteDTO> calculateOptimalRoute(@RequestBody EntityIdRequest request) {
        try {
            if (request.getEntityId() == null) {
                return ResponseEntity.badRequest().build(); // Retorna 400 Bad Request se o ID não for fornecido
            }
            // Chamar o serviço para calcular a rota para a entidade
            RouteDTO optimalRoute = entityService.calculateOptimalRouteForEntity(request.getEntityId());

            if (optimalRoute != null) {
                return ResponseEntity.ok(optimalRoute); // Retorna 200 OK com a rota
            } else {
                // Se o Orquestrador retornar null, significa que nenhuma rota foi encontrada para os critérios da entidade
                return ResponseEntity.notFound().build(); // Retorna 404 Not Found
            }
        } catch (NoSuchElementException e) {
            // Captura a exceção se a entidade não for encontrada pelo ID
            return ResponseEntity.notFound().build(); // Retorna 404 Not Found
        } catch (Exception e) {
            System.err.println("Erro ao calcular rota para entidade: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Retorna 500 Internal Server Error
        }
    }

    /**
     * Endpoint de depuração para redefinir o 'deadline' de todas as entidades UA
     * para a data e hora atuais.
     * Mapeia para requisições POST em /api/entities/debug/reset-deadlines.
     * @return ResponseEntity com uma mensagem de sucesso e o número de entidades atualizadas.
     */
    @PostMapping("/debug/reset-deadlines")
    public ResponseEntity<String> resetDeadlines() {
        try {
            int updatedCount = entityService.resetAllEntityDeadlines();
            return ResponseEntity.ok("Deadlines de " + updatedCount + " entidades redefinidos para a data e hora atuais.");
        } catch (Exception e) {
            System.err.println("Erro ao redefinir prazos: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao redefinir prazos: " + e.getMessage());
        }
    }

    @PostMapping("/debug/reset_deadlines_last")
    public ResponseEntity<String> resetDeadlinesLast() {
        try {
            int updatedCount = entityService.resetAllDeadlinesToLastSlot();
            return ResponseEntity.ok("Deadlines de " + updatedCount + " entidades redefinidos para a data e hora atuais.");
        } catch (Exception e) {
            System.err.println("Erro ao redefinir prazos: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao redefinir prazos: " + e.getMessage());
        }
    }


}
