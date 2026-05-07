package SmartMedSutra.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReminderRequest {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    private Long medicationId;

    @NotBlank(message = "Reminder message is required")
    private String message;

    @NotNull(message = "Reminder time is required")
    private LocalTime reminderTime;

    private String frequency;          // DAILY, WEEKLY, CUSTOM

    private Boolean active;
}
