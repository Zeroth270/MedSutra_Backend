package SmartMedSutra.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoodLogRequest {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @Min(value = 1, message = "Before mood must be between 1 and 5")
    @Max(value = 5, message = "Before mood must be between 1 and 5")
    private int beforeMood;

    @Min(value = 1, message = "After mood must be between 1 and 5")
    @Max(value = 5, message = "After mood must be between 1 and 5")
    private int afterMood;

    @Min(value = 1, message = "Symptom score must be between 1 and 10")
    @Max(value = 10, message = "Symptom score must be between 1 and 10")
    private int symptomScore;
}
