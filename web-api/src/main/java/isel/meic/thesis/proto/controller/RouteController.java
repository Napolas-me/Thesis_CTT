package isel.meic.thesis.proto.controller;

import isel.meic.thesis.proto.dto.RouteDTO;
import isel.meic.thesis.proto.service.RouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@RestController // Marks this class as a REST Controller
@RequestMapping("/api/routes") // Base path for all endpoints in this controller
public class RouteController {

    private final RouteService routeService;

    // Spring will automatically inject RouteService
    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping // Handles GET requests to /api/routes
    public List<RouteDTO> getAllRoutes() {
        return routeService.getAllRoutes();
    }

    @GetMapping("/{id}") // Handles GET requests to /api/routes/{id}
    public ResponseEntity<RouteDTO> getRouteById(@PathVariable Integer id) {
        try {
            RouteDTO route = routeService.getRouteById(id);
            return ResponseEntity.ok(route); // Returns 200 OK with the route
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build(); // Returns 404 Not Found if route not found
        }
    }

    @GetMapping("/{id}/sequence") // Handles GET requests to /api/routes/{id}/sequence
    public ResponseEntity<List<Object>> getRouteSequence(@PathVariable Integer id) {
        try {
            List<Object> sequence = routeService.getRouteSequence(id);
            System.out.println(sequence);
            return ResponseEntity.ok(sequence);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null); // Or a more specific error DTO
        }
    }
}