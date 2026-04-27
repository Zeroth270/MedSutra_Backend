package SmartMedSutra.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntakeLogResponse {

    private Long medLogId;
    private Long medicationId;
    private String medicationName;
    private boolean taken;
    private LocalDateTime takenTime;
    private Integer delayMinutes;
}
