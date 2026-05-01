package SmartMedSutra.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdherenceReportResponse {

    private Long patientId;
    private String patientName;

    // ── Summary ────────────────────────────────────────────────
    private int totalDoses;
    private int takenDoses;
    private int missedDoses;
    private double adherencePercentage;
    private String riskLevel;

    // ── Breakdowns ─────────────────────────────────────────────
    private List<MedicationAdherence> medicationBreakdown;
    private List<DailyAdherence> dailyTrend;

    // ── Nested DTOs ────────────────────────────────────────────

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MedicationAdherence {
        private Long medicationId;
        private String medicationName;
        private String dosage;
        private int totalDoses;
        private int takenDoses;
        private double adherencePercentage;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DailyAdherence {
        private String date;
        private int totalDoses;
        private int takenDoses;
        private double adherencePercentage;
    }
}
