package isel.meic.thesis.proto.orq_global;

import isel.meic.thesis.proto.dataTypes.Route;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class RouteSelector {

    public List<Route> selectRoutes(List<Route> allRoutes, UserParam userParam) { // Renamed entity to userParam for clarity
        List<Route> filteredRoutes = new LinkedList<>();

        for (Route route : allRoutes) {
            // 1. Filter by Origin and Destination
            // Assuming Route has getRouteStartName() and getRouteEndName()
            boolean matchesOrigin = route.getOrigin().equals(userParam.getOrigin());
            boolean matchesDestination = route.getDestination().equals(userParam.getDestination());

            if (!matchesOrigin || !matchesDestination) {
                continue; // Skip if origin or destination doesn't match
            }
            System.out.println("Origin and destination matched for route id = " + route.getId());

            // 2. Filter by Max Transfers
            // IMPORTANT: You need to implement getTransferCount() in your Route class.
            // This method should calculate the number of transfers based on the route's sequence (ligacoes).
            // Example: If a sequence has N trips, it has N-1 transfers.
            boolean meetsMaxTransfers;
            if (userParam.getMaxTransfers() == -1) { // -1 means no limit on transfers
                meetsMaxTransfers = true;
            } else {
                // Assuming route.getTransferCount() returns the actual number of transfers for the route
                meetsMaxTransfers = route.getConnections().size() <= userParam.getMaxTransfers();
                System.out.println("Route id = " + route.getId() + " has " + route.getConnections().size() + " connections, max transfers allowed: " + userParam.getMaxTransfers());
            }

            if (!meetsMaxTransfers) {
                continue; // Skip if it exceeds max transfers
            }
            System.out.println("Max transfers matched for route id = " + route.getId());

            // 3. Filter by Deadline
            // Assuming Route has getRouteEndDate() which represents the final arrival time of the route.
            // This method should return a java.util.Date or java.sql.Timestamp.
            boolean meetsDeadline = true; // Assume true if no deadline is specified
            if (userParam.getDeadline() != null) {
                System.out.println("Checking deadline");
                Date routeArrivalDate = route.getArrivalDate(); // Use the route's end date
                Date userDeadline = userParam.getDeadline();

                // Check if the route's arrival date is on or before the user's deadline
                if (routeArrivalDate == null || routeArrivalDate.after(userDeadline)) {
                    meetsDeadline = false; // Route arrives after the deadline
                    System.out.println("Route id = " + route.getId() + " does not meet the deadline: " + userDeadline);
                    System.out.println("Route arrival date: " + routeArrivalDate);

                }
            }

            if (!meetsDeadline) {
                continue; // Skip if it misses the deadline
            }
            System.out.println("Deadline matched for route id = " + route.getId());

            // If all conditions are met, add the route to the filtered list
            filteredRoutes.add(route);
            System.out.println("Route id = " + route.getId() + " added to filtered routes.");
            System.out.println("Route sequence size: " + route.getConnections().size());
        }
        return filteredRoutes;
    }

    /*public List<Route> selectRoutes(List<Route> allRoutes, UserParam entity) {
        List<Route> filteredRoutes = new LinkedList<>();
        // Lógica de filtragem baseada nos dados da Entity
        for (Route route : allRoutes) {
            // Exemplo: Filtrar por origem e destino
            if (route.getOrigin().equals(entity.getOrigin()) &&
                route.getDestination().equals(entity.getDestination()) &&
                (entity.getMaxTransfers() == -1 || route.getTransport().getCapacity() <= entity.getMaxTransfers()) &&
                (entity.getDeadline() != null || route.getArrivalDate().before(entity.getDeadline()) || route.getArrivalDate().equals(entity.getDeadline()))) {
                filteredRoutes.add(route);
            }
        }
        return filteredRoutes;
    }*/
}
