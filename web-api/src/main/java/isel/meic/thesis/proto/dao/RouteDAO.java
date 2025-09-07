package isel.meic.thesis.proto.dao;

import isel.meic.thesis.proto.dto.RouteDTO;
import isel.meic.thesis.proto.dto.StopDTO;
import isel.meic.thesis.proto.dto.TransportDTO;
import isel.meic.thesis.proto.dto.TripDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Repository // Marks this class as a Spring Data Repository
public class RouteDAO {

    private final JdbcTemplate jdbcTemplate;

    // Spring will automatically inject JdbcTemplate configured from application.properties
    public RouteDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<RouteDTO> findAllRoutes() {
        String sql = "SELECT rd.id, rd.name, rd.route_start_name, rd.route_end_name, " +
                "rd.route_start_date, rd.route_end_date, rd.status, " +
                "t.id AS transport_id, t.type AS transport_type, t.name AS transport_name, " +
                "t.capacity AS transport_capacity, t.max_capacity AS transport_max_capacity, " +
                "t.status AS transport_status, t.carbon_emissions_g_km " +
                "FROM ROUTE_DAILY rd JOIN TRANSPORT t ON rd.transport_id = t.id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRoute(rs));
    }

    public RouteDTO findRouteById(Integer id) {
        String sql = "SELECT rd.id, rd.name, rd.route_start_name, rd.route_end_name, " +
                "rd.route_start_date, rd.route_end_date, rd.status, " +
                "t.id AS transport_id, t.type AS transport_type, t.name AS transport_name, " +
                "t.capacity AS transport_capacity, t.max_capacity AS transport_max_capacity, " +
                "t.status AS transport_status, t.carbon_emissions_g_km " +
                "FROM ROUTE_DAILY rd JOIN TRANSPORT t ON rd.transport_id = t.id WHERE rd.id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> mapRoute(rs));
    }

    public List<Object> getSequenceForRoute(Integer routeId) {
        String sql = "SELECT sequence_order, item_type, item_id FROM SEQUENCE_DAILY WHERE id_route = ? ORDER BY sequence_order";
        return jdbcTemplate.query(sql, new Object[]{routeId}, (rs, rowNum) -> {
            String itemType = rs.getString("item_type");
            Integer itemId = rs.getInt("item_id");

            if ("trip".equalsIgnoreCase(itemType)) {
                return findTripById(itemId);
            } else if ("stop".equalsIgnoreCase(itemType)) {
                return findStopById(itemId);
            } else {
                throw new SQLException("Unknown item type in sequence: " + itemType);
            }
        });
    }

    public TripDTO findTripById(Integer id) {
        String sql = "SELECT id, origin, destination, departure_date, destination_date, status FROM TRIP_DAILY WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            TripDTO trip = new TripDTO();
            trip.setId(rs.getInt("id"));
            trip.setOrigin(rs.getString("origin"));
            trip.setDestination(rs.getString("destination"));
            trip.setDepartureDate(rs.getTimestamp("departure_date").toLocalDateTime());
            trip.setDestinationDate(rs.getTimestamp("destination_date").toLocalDateTime());
            trip.setStatus(rs.getString("status"));
            return trip;
        });
    }

    public StopDTO findStopById(Integer id) {
        String sql = "SELECT id, stop_name, gate_name, arrival_date, departure_date, status FROM STOP_DAILY WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            StopDTO stop = new StopDTO();
            stop.setId(rs.getInt("id"));
            stop.setStopName(rs.getString("stop_name"));
            stop.setGateName(rs.getString("gate_name"));
            stop.setArrivalDate(rs.getTimestamp("arrival_date") != null ? rs.getTimestamp("arrival_date").toLocalDateTime() : null);
            stop.setDepartureDate(rs.getTimestamp("departure_date") != null ? rs.getTimestamp("departure_date").toLocalDateTime() : null);
            stop.setStatus(rs.getString("status"));
            return stop;
        });
    }

    public TransportDTO findTransportById(Integer id) {
        String sql = "SELECT id, type, name, capacity, max_capacity, status, carbon_emissions_g_km FROM TRANSPORT WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            TransportDTO transport = new TransportDTO();
            transport.setId(rs.getInt("id"));
            transport.setType(rs.getString("type"));
            transport.setName(rs.getString("name"));
            transport.setCapacity(rs.getInt("capacity"));
            transport.setMaxCapacity(rs.getInt("max_capacity"));
            transport.setStatus(rs.getString("status"));
            transport.setCarbonEmissionsGkm(rs.getDouble("carbon_emissions_g_km"));
            return transport;
        });
    }


    // Helper method to map ResultSet to RouteDTO
    private RouteDTO mapRoute(ResultSet rs) throws SQLException {
        RouteDTO route = new RouteDTO();
        route.setId(rs.getInt("id"));
        route.setName(rs.getString("name"));
        route.setRouteStartName(rs.getString("route_start_name"));
        route.setRouteEndName(rs.getString("route_end_name"));
        route.setRouteStartDate(rs.getTimestamp("route_start_date").toLocalDateTime());
        route.setRouteEndDate(rs.getTimestamp("route_end_date").toLocalDateTime());
        route.setStatus(rs.getString("status"));

        // Map associated Transport
        TransportDTO transport = new TransportDTO();
        transport.setId(rs.getInt("transport_id"));
        transport.setType(rs.getString("transport_type"));
        transport.setName(rs.getString("transport_name"));
        transport.setCapacity(rs.getInt("transport_capacity"));
        transport.setMaxCapacity(rs.getInt("transport_max_capacity"));
        transport.setStatus(rs.getString("transport_status"));
        transport.setCarbonEmissionsGkm(rs.getDouble("carbon_emissions_g_km"));
        route.setAssignedTransport(transport);

        //get sequence
        route.setSequence(getSequenceForRoute(route.getId()));

        return route;
    }
}