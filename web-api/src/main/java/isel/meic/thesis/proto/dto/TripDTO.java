package isel.meic.thesis.proto.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data; // Requires Lombok dependency

import java.time.LocalDateTime;

@Data
public class TripDTO {
    private final String type = "trip"; // Fixed type for this DTO
    private Integer id;
    private String origin;
    private String destination;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime departureDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime destinationDate;
    private String status;
}