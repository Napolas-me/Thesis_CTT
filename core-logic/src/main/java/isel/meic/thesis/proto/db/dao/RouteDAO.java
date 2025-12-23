package isel.meic.thesis.proto.db.dao;

import isel.meic.thesis.proto.dataTypes.Route;
import isel.meic.thesis.proto.dataTypes.Transport;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteDAO {
    private final Connection conn;

    // Construtor original que recebe a conexão
    public RouteDAO(Connection conn) {
        this.conn = conn;
        // A lógica de clearRoute() e dailyUpdate() ainda está aqui,
        // mas será chamada apenas uma vez pelo Orquestrador no @PostConstruct.
        try {
            clearRoute();
            dailyUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error initializing RouteDAO", e);
        }
    }

    private void dailyUpdate() throws SQLException {
        String selectRouteTemplateSql = "SELECT id, name, route_start_name, route_end_name, route_start_time, transport_id, route_end_time FROM ROUTE_TEMPLATE";
        String insertRouteDailySql = "INSERT INTO ROUTE_DAILY (name, route_start_name, route_end_name, route_start_date, route_end_date, transport_id, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        Map<Integer, Integer> routeTemplateToDailyIdMap = new HashMap<>();
        LocalDate currentDate = LocalDate.now();

        try (PreparedStatement selectRouteTemplateStmt = conn.prepareStatement(selectRouteTemplateSql);
             ResultSet rs = selectRouteTemplateStmt.executeQuery();
             PreparedStatement insertRouteDailyStmt = conn.prepareStatement(insertRouteDailySql, Statement.RETURN_GENERATED_KEYS)) {

            while (rs.next()) {
                int templateRouteId = rs.getInt("id");
                String name = rs.getString("name");
                String startName = rs.getString("route_start_name");
                String endName = rs.getString("route_end_name");
                Time startTime = rs.getTime("route_start_time");
                Time endTime = rs.getTime("route_end_time");
                Integer transport_id = rs.getInt("transport_id");

                Timestamp startDate = Timestamp.valueOf(currentDate.atTime(startTime.toLocalTime()));
                Timestamp endDate = Timestamp.valueOf(currentDate.atTime(endTime.toLocalTime()));

                insertRouteDailyStmt.setString(1, name);
                insertRouteDailyStmt.setString(2, startName);
                insertRouteDailyStmt.setString(3, endName);
                insertRouteDailyStmt.setTimestamp(4, startDate);
                insertRouteDailyStmt.setTimestamp(5, endDate);
                if (transport_id == 0 && rs.wasNull()) {
                    insertRouteDailyStmt.setNull(6, Types.INTEGER);
                } else {
                    insertRouteDailyStmt.setInt(6, transport_id);
                }
                insertRouteDailyStmt.setString(7, "scheduled");
                insertRouteDailyStmt.executeUpdate();

                try (ResultSet generatedKeys = insertRouteDailyStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int newRouteDailyId = generatedKeys.getInt(1);
                        routeTemplateToDailyIdMap.put(templateRouteId, newRouteDailyId);
                    }
                }
            }
        }

        String selectSeqTemplateSql = "SELECT id_template, sequence_order, item_type, item_id FROM SEQUENCE_TEMPLATE";
        String insertSeqDailySql = "INSERT INTO SEQUENCE_DAILY (id_route, sequence_order, item_type, item_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement selectSeqTemplateStmt = conn.prepareStatement(selectSeqTemplateSql);
             ResultSet seqRs = selectSeqTemplateStmt.executeQuery();
             PreparedStatement insertSeqDailyStmt = conn.prepareStatement(insertSeqDailySql)) {

            while (seqRs.next()) {
                int templateId = seqRs.getInt("id_template");
                int sequenceOrder = seqRs.getInt("sequence_order");
                String itemType = seqRs.getString("item_type");
                int itemId = seqRs.getInt("item_id");

                Integer correspondingRouteDailyId = routeTemplateToDailyIdMap.get(templateId);

                if (correspondingRouteDailyId != null) {
                    insertSeqDailyStmt.setInt(1, correspondingRouteDailyId);
                    insertSeqDailyStmt.setInt(2, sequenceOrder);
                    insertSeqDailyStmt.setString(3, itemType);
                    insertSeqDailyStmt.setInt(4, itemId);
                    insertSeqDailyStmt.executeUpdate();
                } else {
                    System.err.println("Warning: SEQUENCE_TEMPLATE with id_template " + templateId + " has no corresponding ROUTE_DAILY. Skipped.");
                }
            }
        }
    }

    public List<Object> getSequenceForRoute(int routeId) throws SQLException {
        String sql = "SELECT sequence_order, item_type, item_id FROM SEQUENCE_DAILY WHERE id_route = ? ORDER BY sequence_order";
        List<Object> sequence = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, routeId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String itemType = rs.getString("item_type");
                    int itemId = rs.getInt("item_id");

                    Object actualItem = null;
                    // Instantiating DAOs here is less efficient, but keeps them as non-Spring beans.
                    // For better performance, consider injecting TripDAO and StopDAO into RouteDAO's constructor.
                    TripDAO tripDAO = new TripDAO(conn);
                    StopDAO stopDAO = new StopDAO(conn);

                    switch (itemType.toLowerCase()) {
                        case "trip":
                            actualItem = tripDAO.getTripById(itemId);
                            break;
                        case "stop":
                            actualItem = stopDAO.getStopById(itemId);
                            break;
                        default:
                            throw new SQLException("Unknown item type in sequence: " + itemType);
                    }

                    if (actualItem != null) {
                        sequence.add(actualItem);
                    } else {
                        System.err.println("Warning: Item ID " + itemId + " of type " + itemType + " not found in database or object creation failed.");
                    }
                }
            }
        }
        return sequence;
    }

    public List<Route> getAllRoutes() throws SQLException {
        String route_data = "SELECT * FROM ROUTE_DAILY";
        List<Route> routes = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(route_data)) {
            while (rs.next()) {
                // Instantiating TransportDAO here is less efficient.
                // Consider injecting TransportDAO into RouteDAO's constructor.
                TransportDAO transportDAO = new TransportDAO(conn);
                Transport newTransport = transportDAO.getTransportById(rs.getInt("transport_id"));

                Route route = new Route(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("route_start_name"),
                        rs.getString("route_end_name"),
                        rs.getTimestamp("route_start_date"),
                        rs.getTimestamp("route_end_date"),
                        newTransport,
                        rs.getString("status")
                );
                // Populate the sequence (ligacoes) for the route
                route.setLigacoes(getSequenceForRoute(route.getId())); // ADDED: Populate sequence
                routes.add(route);
            }
        }
        return routes;
    }

    // Novo método para buscar rotas por origem e destino
    public List<Route> findRoutesByOriginAndDestination(String origin, String destination) throws SQLException {
        String sql = "SELECT rd.id, rd.name, rd.route_start_name, rd.route_end_name, " +
                "rd.route_start_date, rd.route_end_date, rd.status, rd.transport_id " +
                "FROM ROUTE_DAILY rd WHERE rd.route_start_name = ? AND rd.route_end_name = ?";

        List<Route> routes = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, origin);
            stmt.setString(2, destination);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Instantiating TransportDAO here is less efficient.
                    // Consider injecting TransportDAO into RouteDAO's constructor.
                    TransportDAO transportDAO = new TransportDAO(conn);
                    Transport assignedTransport = transportDAO.getTransportById(rs.getInt("transport_id"));

                    Route route = new Route(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("route_start_name"),
                            rs.getString("route_end_name"),
                            rs.getTimestamp("route_start_date"),
                            rs.getTimestamp("route_end_date"),
                            assignedTransport,
                            rs.getString("status")
                    );
                    // Populate the sequence (ligacoes) for the route
                    route.setLigacoes(getSequenceForRoute(route.getId())); // ADDED: Populate sequence
                    routes.add(route);
                }
            }
        }
        return routes;
    }


    private void clearRoute() throws SQLException {
        String deleteSequenceSql = "DELETE FROM SEQUENCE_DAILY";
        String resetSequenceAutoIncrementSql = "ALTER TABLE SEQUENCE_DAILY AUTO_INCREMENT = 1";

        String deleteRouteDailySql = "DELETE FROM ROUTE_DAILY";
        String resetRouteDailyAutoIncrementSql = "ALTER TABLE ROUTE_DAILY AUTO_INCREMENT = 1";

        try (Statement stmt = conn.createStatement()) {
            System.out.println("Clearing SEQUENCE_DAILY table...");
            stmt.executeUpdate(deleteSequenceSql);
            System.out.println("SEQUENCE_DAILY table cleared and AUTO_INCREMENT reset."); // FIXED: stmt.println to System.out.println

            System.out.println("Clearing ROUTE_DAILY table...");
            stmt.executeUpdate(deleteRouteDailySql);
            stmt.executeUpdate(resetRouteDailyAutoIncrementSql);
            System.out.println("ROUTE_DAILY table cleared and AUTO_INCREMENT reset.");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error clearing ROUTE_DAILY and SEQUENCE_DAILY tables. Check deletion order or constraints.", e);
        }
    }
}
