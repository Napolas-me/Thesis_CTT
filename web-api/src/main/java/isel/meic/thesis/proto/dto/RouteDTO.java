package isel.meic.thesis.proto.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data; // Requires Lombok dependency

import java.time.LocalDateTime;
import java.util.List;

@Data // Generates getters, setters, equals, hashCode, toString
public class RouteDTO {
    private Integer id;
    private String name;
    private String routeStartName;
    private String routeEndName;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime routeStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime routeEndDate;
    private TransportDTO assignedTransport; // DTO for transport
    private String status;
    private List<Object> sequence; // Can be TripDTO or StopDTO
}