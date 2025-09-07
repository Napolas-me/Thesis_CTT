package isel.meic.thesis.proto.orq_global;

import isel.meic.thesis.proto.dataTypes.*;
import isel.meic.thesis.proto.dataTypes.metrics.Metric;
import isel.meic.thesis.proto.db.dao.*; // Importar todas as DAOs do core-logic (as que usam Connection)



import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Component // Torna Orquestrador um bean Spring
public class Orquestrador {
    private Connection dbConnection;
    private RouteDAO routeDAO;
    private TransportDAO transportDAO;
    private TripDAO tripDAO;
    private StopDAO stopDAO;

    public Orquestrador() {
        // A conexão e inicialização serão feitas no método @PostConstruct
    }

    @PostConstruct
    private void init() {
        try {
            this.dbConnection = DBConnection.connect(); // DBConnection.connect() ainda é usado aqui
        } catch (Exception e) {
            throw new RuntimeException("Error opening connection to DB", e);
        }
        StartUpDaily(); // Inicializa os DAOs e dados diários
    }

    private void StartUpDaily(){
        // Instanciar os DAOs do core-logic com a conexão manual
        tripDAO = new TripDAO(dbConnection);
        stopDAO = new StopDAO(dbConnection);
        routeDAO = new RouteDAO(dbConnection);
        transportDAO = new TransportDAO(dbConnection);
    }

    public Route process(Entity entity) { // Usar UserParam diretamente
        System.out.println("got entity: " + entity.toString());
        System.out.println("UserParams: " + entity.getUserParam().toString());

        // get metric
        MetricSelectionStrategy metricSelector = new DefaultMetricSelectionStrategy();
        Metric chosenMetric = metricSelector.selectMetric(entity);

        System.out.println("metric chosen: " + chosenMetric.getClass());

        // get routes from the database according to the user request
        List<Route> allRoutes;
        try {
            // Usar o novo método findRoutesByOriginAndDestination do RouteDAO
            allRoutes = routeDAO.findRoutesByOriginAndDestination(entity.getOrigin(), entity.getDestination());
            System.out.println("Routes availabe: " + allRoutes.size());
            // Popula as sequências para as rotas obtidas
            //for (Route route : allRoutes) {
                //route.setLigacoes(routeDAO.getSequenceForRoute(route.getId()));
                // O transporte já deve vir populado pelo RouteDAO.findRoutesByOriginAndDestination
                // ou RouteDAO.getAllRoutes, se o RowMapper o fizer.
                // Se o getTransportById for chamado aqui, certifique-se que não é redundante.
                // route.setTransport(transportDAO.getTransportById(route.getTransport().getId())); // Provavelmente não é mais necessário aqui
           //}
        } catch (SQLException e) {
            throw new RuntimeException("Error getting routes from DB", e);
        }

        //select the best route based on the user request
        RouteSelector routeSelector = new RouteSelector();
        List<Route> selectedRoutes = routeSelector.selectRoutes(allRoutes, entity.getUserParam());
        System.out.println("Routes checked: " + selectedRoutes.size());

        List<Route> sortedRoutes = chosenMetric.sort(selectedRoutes);
        System.out.println("Sorted Routes: " + sortedRoutes.size());

        if (!sortedRoutes.isEmpty()) {
            //check if the transport in each route has space available
            //if no space is available, remove the route from the list
            sortedRoutes.removeIf(route -> route.getTransport() == null || !route.getTransport().canAdd(1));

            System.out.println("Selected " + sortedRoutes.size() + " routes based on the user's request:");
            sortedRoutes.forEach(route -> {
                System.out.println("\tRoute ID: " + route.getId() + ", Name: " + route.getName());
            });
            return sortedRoutes.get(0);
        } else {
            // Lidar com o caso de não haver rotas
            System.out.println("No routes found for the given criteria.");
            return null;
        }
    }

    public void acceptRoute(Route route) {
        Transport t = route.getTransport();
        if (t != null) {
            t.add(1); // Add one unit of cargo to the transport
            try {
                transportDAO.updateTransport(t); // Update the transport in the database
            } catch (SQLException e) {
                throw new RuntimeException("Error updating transport in DB", e);
            }
        } else {
            System.err.println("Cannot accept route " + route.getId() + ": No transport assigned or transport is null.");
        }
        //maybe do more in the future
    }

    public void closeConnectionDB() {
        try {
            if (dbConnection != null && !dbConnection.isClosed()) {
                dbConnection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error closing connection to DB", e);
        }
    }
}
