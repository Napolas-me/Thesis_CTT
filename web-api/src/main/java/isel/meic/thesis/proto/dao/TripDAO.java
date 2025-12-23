package isel.meic.thesis.proto.dao;

import isel.meic.thesis.proto.dto.RouteDTO;
import isel.meic.thesis.proto.dto.StopDTO;
import isel.meic.thesis.proto.dto.TransportDTO;
import isel.meic.thesis.proto.dto.TripDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository // Marks this class as a Spring Data Repository
public class TripDAO {

    private final JdbcTemplate jdbcTemplate;

    // Spring will automatically inject JdbcTemplate configured from application.properties
    public TripDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<TripDTO> findAllTrips(){
        String sql = "SELECT * FROM TRIP_DAILY";
        return jdbcTemplate.query(sql, mapTrip);
    }

    private void updateTripDates(TripDTO trip) {
        String sql = "UPDATE TRIP_DAILY SET departure_date = ?, destination_date = ? WHERE id = ?";

        int updatedRows = jdbcTemplate.update(sql,
                Timestamp.valueOf(trip.getDepartureDate()),
                Timestamp.valueOf(trip.getDestinationDate()),
                trip.getId()
        );

        if (updatedRows > 0) {
            System.out.println("UPDATED DB: Route ID " + trip.getId() + " to Start: " + trip.getDepartureDate());
        } else {
            System.out.println("Warning: Could not update Route ID " + trip.getId());
        }
    }

    public void dailyUpdateTrip(){
        List<TripDTO> allTrips = findAllTrips();
        LocalDate today = LocalDate.now();

        System.out.println("Processing update for date: " + today);
        System.out.println("------------------------------------------");

        for (TripDTO trip : allTrips){

            // --- 1. Update the Start Date/Time ---
            LocalDateTime existingStartTime = trip.getDepartureDate();

            // Key step: Combine today's date with the existing time
            LocalDateTime newStartDateTime = today.atTime(existingStartTime.toLocalTime());

            trip.setDepartureDate(newStartDateTime);


            // --- 2. Update the End Date/Time ---
            LocalDateTime existingEndTime = trip.getDestinationDate();
            LocalDateTime newEndDateTime = today.atTime(existingEndTime.toLocalTime());

            // Check for overnight routes (where end time is earlier than start time)
            if (newEndDateTime.toLocalTime().isBefore(newStartDateTime.toLocalTime())) {
                // If it's an overnight route, the end date must be tomorrow
                newEndDateTime = newEndDateTime.plusDays(1);
            }

            trip.setDestinationDate(newEndDateTime);

            // --- 3. Save the changes ---
            updateTripDates(trip);
        }

        System.out.println("------------------------------------------");
        System.out.println("Daily Trip date update finished.");
    }

    private final RowMapper<TripDTO> mapTrip = (rs, rowNum) ->{
        TripDTO trip = new TripDTO();

        trip.setId(rs.getInt("id"));
        trip.setOrigin(rs.getString("origin"));
        trip.setDestination(rs.getString("destination"));
        trip.setDepartureDate(rs.getTimestamp("departure_date").toLocalDateTime());
        trip.setDestinationDate(rs.getTimestamp("destination_date").toLocalDateTime());
        trip.setStatus(rs.getString("status"));

        return trip;
    };
}
