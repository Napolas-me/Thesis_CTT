package isel.meic.thesis.proto.dto;

import lombok.Data; // Requires Lombok dependency

import java.time.LocalDateTime;

@Data
public class TripDTO {
    private final String tripType = "trip"; // Fixed type for this DTO
    private Integer id;
    private String origin;
    private String destination;
    private LocalDateTime departureDate;
    private LocalDateTime destinationDate;
    private String status;
}