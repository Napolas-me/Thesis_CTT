package isel.meic.thesis.proto.dto;

import lombok.Data; // Requires Lombok dependency

import java.time.LocalDateTime;

@Data
public class StopDTO {
    private final String stopType = "stop"; // Fixed type for this DTO
    private Integer id;
    private String stopName;
    private String gateName;
    private LocalDateTime arrivalDate;
    private LocalDateTime departureDate;
    private String status;
}