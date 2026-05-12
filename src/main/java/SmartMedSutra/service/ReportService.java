package SmartMedSutra.service;

import SmartMedSutra.dto.AdherenceReportResponse;
import SmartMedSutra.dto.AdherenceReportResponse.*;
import SmartMedSutra.dto.HealthReportResponse;
import SmartMedSutra.entity.Medication;
import SmartMedSutra.entity.MedicationLog;
import SmartMedSutra.entity.MoodLog;
import SmartMedSutra.entity.User;
import SmartMedSutra.repository.MedicationLogRepository;
import SmartMedSutra.repository.MedicationRepository;
import SmartMedSutra.repository.MoodLogRepository;
import SmartMedSutra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final UserRepository userRepository;
    private final MedicationRepository medicationRepository;
    private final MedicationLogRepository medicationLogRepository;
    private final MoodLogRepository moodLogRepository;

    // ── Adherence Report ───────────────────────────────────────

    public AdherenceReportResponse getAdherenceReport(Long patientId) {
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        List<MedicationLog> allLogs = medicationLogRepository.findByPatientId(patientId);
        List<Medication> medications = medicationRepository.findByPatientId(patientId);

        int totalDoses = allLogs.size();
        int takenDoses = (int) allLogs.stream().filter(MedicationLog::isTaken).count();
        int missedDoses = totalDoses - takenDoses;
        double adherence = totalDoses > 0 ? (takenDoses * 100.0) / totalDoses : 0.0;

        // Per-medication breakdown         
        List<MedicationAdherence> medBreakdown = medications.stream()
                .map(med -> {
                    List<MedicationLog> medLogs = allLogs.stream()
                            .filter(log -> log.getMedication().getMedId().equals(med.getMedId()))
                            .toList();
                    int medTotal = medLogs.size();
                    int medTaken = (int) medLogs.stream().filter(MedicationLog::isTaken).count();
                    return MedicationAdherence.builder()
                            .medicationId(med.getMedId())
                            .medicationName(med.getName())
                            .dosage(med.getDosage())
                            .totalDoses(medTotal)
                            .takenDoses(medTaken)
                            .adherencePercentage(medTotal > 0 ? Math.round((medTaken * 100.0) / medTotal * 100.0) / 100.0 : 0.0)
                            .build();
                })
                .toList();

        // Daily trend (group by date)
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Map<String, List<MedicationLog>> dailyGroups = allLogs.stream()
                .filter(log -> log.getTakenTime() != null)
                .collect(Collectors.groupingBy(log -> log.getTakenTime().toLocalDate().format(dateFmt)));

        List<DailyAdherence> dailyTrend = dailyGroups.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    int dTotal = entry.getValue().size();
                    int dTaken = (int) entry.getValue().stream().filter(MedicationLog::isTaken).count();
                    return DailyAdherence.builder()
                            .date(entry.getKey())
                            .totalDoses(dTotal)
                            .takenDoses(dTaken)
                            .adherencePercentage(dTotal > 0 ? Math.round((dTaken * 100.0) / dTotal * 100.0) / 100.0 : 0.0)
                            .build();
                })
                .toList();

        String riskLevel = adherence >= 85 ? "LOW" : adherence >= 70 ? "MODERATE" : adherence >= 50 ? "HIGH" : "CRITICAL";

        return AdherenceReportResponse.builder()
                .patientId(patient.getId())
                .patientName(patient.getName())
                .totalDoses(totalDoses)
                .takenDoses(takenDoses)
                .missedDoses(missedDoses)
                .adherencePercentage(Math.round(adherence * 100.0) / 100.0)
                .riskLevel(riskLevel)
                .medicationBreakdown(medBreakdown)
                .dailyTrend(dailyTrend)
                .build();
    }

    // ── Health Report ──────────────────────────────────────────

    public HealthReportResponse getHealthReport(Long patientId) {
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        List<MoodLog> moodLogs = moodLogRepository.findByPatientIdOrderByTimestampDesc(patientId);

        double avgBefore = moodLogs.stream().mapToInt(MoodLog::getBeforeMood).average().orElse(0.0);
        double avgAfter = moodLogs.stream().mapToInt(MoodLog::getAfterMood).average().orElse(0.0);
        double avgSymptom = moodLogs.stream().mapToInt(MoodLog::getSymptomScore).average().orElse(0.0);

        // Determine trend from last 5 entries
        String trend = determineTrend(moodLogs);
        String recommendation = generateRecommendation(trend, avgSymptom);

        List<HealthReportResponse.MoodEntry> recentEntries = moodLogs.stream()
                .limit(20)
                .map(log -> HealthReportResponse.MoodEntry.builder()
                        .moodId(log.getMoodId())
                        .beforeMood(log.getBeforeMood())
                        .afterMood(log.getAfterMood())
                        .symptomScore(log.getSymptomScore())
                        .timestamp(log.getTimestamp())
                        .build())
                .toList();

        return HealthReportResponse.builder()
                .patientId(patient.getId())
                .patientName(patient.getName())
                .averageBeforeMood(Math.round(avgBefore * 100.0) / 100.0)
                .averageAfterMood(Math.round(avgAfter * 100.0) / 100.0)
                .averageSymptomScore(Math.round(avgSymptom * 100.0) / 100.0)
                .totalMoodEntries(moodLogs.size())
                .recentMoodLogs(recentEntries)
                .overallTrend(trend)
                .recommendation(recommendation)
                .build();
    }

    // ── Helpers ─────────────────────────────────────────────────

    private String determineTrend(List<MoodLog> moodLogs) {
        if (moodLogs.size() < 3) return "STABLE";

        List<MoodLog> recent = moodLogs.stream().limit(5).toList();
        double recentAvg = recent.stream().mapToInt(MoodLog::getAfterMood).average().orElse(0);
        double overallAvg = moodLogs.stream().mapToInt(MoodLog::getAfterMood).average().orElse(0);

        if (recentAvg > overallAvg + 0.5) return "IMPROVING";
        if (recentAvg < overallAvg - 0.5) return "DECLINING";
        return "STABLE";
    }

    private String generateRecommendation(String trend, double avgSymptom) {
        if (avgSymptom >= 7) {
            return "High symptom scores detected. Consider scheduling a consultation with your healthcare provider.";
        }
        return switch (trend) {
            case "IMPROVING" -> "Great progress! Your mood is improving. Continue your current medication routine.";
            case "DECLINING" -> "Your mood trend is declining. Consider discussing medication adjustments with your doctor.";
            default -> "Your health metrics are stable. Keep following your prescribed schedule.";
        };
    }
}
