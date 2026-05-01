package SmartMedSutra.dto;

import SmartMedSutra.entity.AlertStatus;
import SmartMedSutra.entity.Urgency;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertResponse {

    private Long id;
    private Long patientId;
    private String patientName;
    private String message;
    private Urgency urgency;
    private AlertStatus status;
    private LocalDateTime createdAt;
}
