package SmartMedSutra.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicationResponse {

    private Long medId;
    private Long patientId;
    private String patientName;
    private String name;
    private String dosage;
    private LocalTime time;
    private String frequency;
    private LocalDate startDate;
    private LocalDate endDate;
}
