package SmartMedSutra.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntakeLogRequest {

    @NotNull(message = "Medication ID is required")
    private Long medicationId;

    @NotNull(message = "Taken status is required")
    private Boolean taken;

    private Integer delayMinutes;
}
