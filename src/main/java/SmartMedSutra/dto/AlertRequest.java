package SmartMedSutra.dto;

import SmartMedSutra.entity.Urgency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertRequest {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotBlank(message = "Alert message is required")
    private String message;

    @NotNull(message = "Urgency level is required")
    private Urgency urgency;
}
