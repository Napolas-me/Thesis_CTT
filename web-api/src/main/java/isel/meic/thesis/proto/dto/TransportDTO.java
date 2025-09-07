package isel.meic.thesis.proto.dto;

import lombok.Data; // Requires Lombok dependency

@Data
public class TransportDTO {
    private Integer id;
    private String type; // e.g., "truck", "car", "motorcycle"
    private String name;
    private Integer capacity;
    private Integer maxCapacity;
    private String status;
    private Double carbonEmissionsGkm;
}