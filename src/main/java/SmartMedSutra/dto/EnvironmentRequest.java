package SmartMedSutra.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnvironmentRequest {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotBlank(message = "Location is required")
    private String location;
}
