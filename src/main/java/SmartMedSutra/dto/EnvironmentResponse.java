package SmartMedSutra.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnvironmentResponse {

    private Long environmentId;
    private Long patientId;
    private String patientName;
    private Double aqi;
    private Double temperature;
    private Double humidity;
    private String location;
    private LocalDateTime timestamp;
}
