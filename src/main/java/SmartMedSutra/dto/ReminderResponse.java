package SmartMedSutra.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReminderResponse {

    private Long id;
    private Long patientId;
    private String patientName;
    private Long medicationId;
    private String medicationName;
    private String message;
    private LocalTime reminderTime;
    private String frequency;
    private boolean active;
    private LocalDateTime createdAt;
}
