package SmartMedSutra.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoodLogResponse {

    private Long moodId;
    private Long patientId;
    private String patientName;
    private int beforeMood;
    private int afterMood;
    private int symptomScore;
    private LocalDateTime timestamp;
}
