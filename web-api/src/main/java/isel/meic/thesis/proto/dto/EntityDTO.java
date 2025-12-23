package isel.meic.thesis.proto.dto;

import lombok.Data; // Requires Lombok dependency
import com.fasterxml.jackson.annotation.JsonFormat; // Import JsonFormat annotation
import java.time.LocalDateTime;

@Data
public class EntityDTO {

    private Integer id;
    private String name;
    private String description;

    // Explicitly format LocalDateTime to ISO 8601 string
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDate;

    // Explicitly format LocalDateTime to ISO 8601 string
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedDate;

    private String type;
    private String origin;
    private String destination;
    private Integer maxTransfers;

    // Explicitly format LocalDateTime to ISO 8601 string
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deadline;

    private int routeId;
    private String status;
}

