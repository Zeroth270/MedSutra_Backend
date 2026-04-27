package SmartMedSutra.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicationRequest {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotBlank(message = "Medication name is required")
    private String name;

    private String dosage;

    private LocalTime time;

    private String frequency;

    private LocalDate startDate;

    private LocalDate endDate;
}
