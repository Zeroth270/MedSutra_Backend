package SmartMedSutra.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardResponse {

    // ── Patient Info ───────────────────────────────────────────
    private Long patientId;
    private String patientName;

    // ── Adherence ──────────────────────────────────────────────
    private double adherencePercentage;
    private int totalDoses;
    private int takenDoses;
    private int missedDoses;

    // ── Risk Assessment ────────────────────────────────────────
    private String riskLevel;          // LOW, MODERATE, HIGH, CRITICAL
    private String riskDescription;

    // ── Recent Logs ────────────────────────────────────────────
    private List<RecentIntakeLog> recentIntakeLogs;
    private List<RecentMoodLog> recentMoodLogs;

    // ── Alerts ─────────────────────────────────────────────────
    private List<Alert> alerts;

    // ── Nested DTOs ────────────────────────────────────────────

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RecentIntakeLog {
        private Long medLogId;
        private String medicationName;
        private boolean taken;
        private LocalDateTime takenTime;
        private Integer delayMinutes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RecentMoodLog {
        private Long moodId;
        private int beforeMood;
        private int afterMood;
        private int symptomScore;
        private LocalDateTime timestamp;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Alert {
        private String type;           // MISSED_DOSE, LOW_MOOD, HIGH_SYMPTOM, DELAYED_DOSE
        private String severity;       // INFO, WARNING, CRITICAL
        private String message;
        private LocalDateTime timestamp;
    }
}
