package isel.meic.thesis.proto.service;

import isel.meic.thesis.proto.dao.RouteDAO;
import isel.meic.thesis.proto.dto.RouteDTO;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
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

    public void dailyUpdateRouteDate(){
        routeDAO.dailyUpdateRoute();
    }
}