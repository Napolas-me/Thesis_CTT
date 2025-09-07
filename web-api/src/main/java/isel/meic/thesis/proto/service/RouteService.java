package isel.meic.thesis.proto.service;

import isel.meic.thesis.proto.dao.RouteDAO;
import isel.meic.thesis.proto.dto.RouteDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service // Marks this class as a Spring Service
public class RouteService {

    private final RouteDAO routeDAO;

    // Spring will automatically inject RouteDAO
    public RouteService(RouteDAO routeDAO) {
        this.routeDAO = routeDAO;
    }

    public List<RouteDTO> getAllRoutes() {
        return routeDAO.findAllRoutes();
    }

    public RouteDTO getRouteById(Integer id) {
        return routeDAO.findRouteById(id);
    }

    public List<Object> getRouteSequence(Integer routeId) {
        return routeDAO.getSequenceForRoute(routeId);
    }

    // You could add methods here for route selection logic
    // For example:
    /*
    public List<RouteDTO> selectRoutes(UserParamDTO userParam) {
        // This would involve fetching routes, applying your MetricSelectionStrategy,
        // and returning the sorted/filtered list of RouteDTOs.
        // This would require adapting your existing "Orquestrador" and "RouteSelector" logic
        // to work within this service layer, potentially using Spring components.
        return List.of(); // Placeholder
    }
    */
}