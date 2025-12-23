package isel.meic.thesis.proto.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data; // Requires Lombok dependency

import java.time.LocalDateTime;

@Data
public class StopDTO {
    private final String type = "stop"; // Fixed type for this DTO
    private Integer id;
    private String stopName;
    private String gateName;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime arrivalDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime departureDate;
    private String status;
}