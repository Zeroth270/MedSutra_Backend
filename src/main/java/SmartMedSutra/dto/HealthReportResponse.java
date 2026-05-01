package SmartMedSutra.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthReportResponse {

    private Long patientId;
    private String patientName;

    // ── Mood Summary ───────────────────────────────────────────
    private double averageBeforeMood;
    private double averageAfterMood;
    private double averageSymptomScore;
    private int totalMoodEntries;

    // ── Trends ─────────────────────────────────────────────────
    private List<MoodEntry> recentMoodLogs;

    // ── Insights ───────────────────────────────────────────────
    private String overallTrend;           // IMPROVING, STABLE, DECLINING
    private String recommendation;

    // ── Nested DTO ─────────────────────────────────────────────

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MoodEntry {
        private Long moodId;
        private int beforeMood;
        private int afterMood;
        private int symptomScore;
        private LocalDateTime timestamp;
    }
}
