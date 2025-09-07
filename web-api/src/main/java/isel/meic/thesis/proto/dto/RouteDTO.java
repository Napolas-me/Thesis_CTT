package isel.meic.thesis.proto.dto;

import lombok.Data; // Requires Lombok dependency

import java.time.LocalDateTime;
import java.util.List;

@Data // Generates getters, setters, equals, hashCode, toString
public class RouteDTO {
    private Integer id;
    private String name;
    private String routeStartName;
    private String routeEndName;
    private LocalDateTime routeStartDate;
    private LocalDateTime routeEndDate;
    private TransportDTO assignedTransport; // DTO for transport
    private String status;
    private List<Object> sequence; // Can be TripDTO or StopDTO
}