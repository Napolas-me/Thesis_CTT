package isel.meic.thesis.proto.dao;

import isel.meic.thesis.proto.dto.StopDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository // Marks this class as a Spring Data Repository
public class StopDAO {

    private final JdbcTemplate jdbcTemplate;

    // Spring will automatically inject JdbcTemplate configured from application.properties
    public StopDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<StopDTO> findAllStops(){
        String sql = "SELECT * FROM STOP_DAILY";
        return jdbcTemplate.query(sql, mapStop);
    }

    private void updateStopDates(StopDTO stop) {
        String sql = "UPDATE STOP_DAILY SET arrival_date = ?, departure_date = ? WHERE id = ?";

        int updatedRows = jdbcTemplate.update(sql,
                Timestamp.valueOf(stop.getArrivalDate()),
                Timestamp.valueOf(stop.getDepartureDate()),
                stop.getId()
        );

        if (updatedRows > 0) {
            System.out.println("UPDATED DB: Stop ID " + stop.getId() + " to Start: " + stop.getDepartureDate());
        } else {
            System.out.println("Warning: Could not update Stop ID " + stop.getId());
        }
    }

    public void dailyUpdateStop(){
        List<StopDTO> allStops = findAllStops();
        LocalDate today = LocalDate.now();

        System.out.println("Processing update for date: " + today);
        System.out.println("------------------------------------------");

        for (StopDTO stop : allStops){

            // --- 1. Update the Start Date/Time ---
            LocalDateTime existingStartTime = stop.getArrivalDate();

            // Key step: Combine today's date with the existing time
            LocalDateTime newStartDateTime = today.atTime(existingStartTime.toLocalTime());

            stop.setArrivalDate(newStartDateTime);


            // --- 2. Update the End Date/Time ---
            LocalDateTime existingEndTime = stop.getDepartureDate();
            LocalDateTime newEndDateTime = today.atTime(existingEndTime.toLocalTime());

            // Check for overnight routes (where end time is earlier than start time)
            if (newEndDateTime.toLocalTime().isBefore(newStartDateTime.toLocalTime())) {
                // If it's an overnight route, the end date must be tomorrow
                newEndDateTime = newEndDateTime.plusDays(1);
            }

            stop.setDepartureDate(newEndDateTime);

            // --- 3. Save the changes ---
            updateStopDates(stop);
        }

        System.out.println("------------------------------------------");
        System.out.println("Daily Stop date update finished.");
    }

    private final RowMapper<StopDTO> mapStop = (rs, rowNum) ->{
        StopDTO stop = new StopDTO();

        stop.setId(rs.getInt("id"));
        stop.setStopName(rs.getString("stop_name"));
        stop.setGateName(rs.getString("gate_name"));
        stop.setArrivalDate(rs.getTimestamp("arrival_date").toLocalDateTime());
        stop.setDepartureDate(rs.getTimestamp("departure_date").toLocalDateTime());
        stop.setStatus(rs.getString("status"));

        return stop;
    };
}
