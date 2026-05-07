package SmartMedSutra.service;

import SmartMedSutra.dto.CaregiverDashboardResponse;
import SmartMedSutra.dto.DashboardResponse;
import SmartMedSutra.dto.DashboardResponse.*;
import SmartMedSutra.dto.DoctorDashboardResponse;
import SmartMedSutra.entity.*;
import SmartMedSutra.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final MedicationLogRepository medicationLogRepository;
    private final MoodLogRepository moodLogRepository;
    private final PatientRelationshipRepository relationshipRepository;
    private final ReminderRepository reminderRepository;

    // ── Patient Dashboard ──────────────────────────────────────

    public DashboardResponse getPatientDashboard(Long patientId) {
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        return buildDashboard(patient);
    }

    // ── Caregiver Dashboard ────────────────────────────────────

    public CaregiverDashboardResponse getCaregiverDashboard(Long caregiverId) {
        User caregiver = userRepository.findById(caregiverId)
                .orElseThrow(() -> new RuntimeException("Caregiver not found with id: " + caregiverId));

        List<PatientRelationship> relationships = relationshipRepository.findByCaregiverId(caregiverId);

        List<DashboardResponse> patientDashboards = relationships.stream()
                .map(rel -> buildDashboard(rel.getPatient()))
                .toList();

        return CaregiverDashboardResponse.builder()
                .caregiverId(caregiver.getId())
                .caregiverName(caregiver.getName())
                .patientDashboards(patientDashboards)
                .build();
    }

    // ── Doctor Dashboard ───────────────────────────────────────

    public DoctorDashboardResponse getDoctorDashboard(Long doctorId) {
        User doctor = userRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorId));

        List<PatientRelationship> relationships = relationshipRepository.findByDoctorId(doctorId);

        List<DashboardResponse> patientDashboards = relationships.stream()
                .map(rel -> buildDashboard(rel.getPatient()))
                .toList();

        return DoctorDashboardResponse.builder()
                .doctorId(doctor.getId())
                .doctorName(doctor.getName())
                .patientDashboards(patientDashboards)
                .build();
    }

    // ── All Patients Dashboard ─────────────────────────────────

    public List<DashboardResponse> getAllPatientsDashboard() {
        List<User> patients = userRepository.findByRole(Role.PATIENT);
        return patients.stream()
                .map(this::buildDashboard)
                .toList();
    }

    // ── Build Dashboard for a Patient ──────────────────────────

    private DashboardResponse buildDashboard(User patient) {
        List<MedicationLog> intakeLogs = medicationLogRepository.findByPatientId(patient.getId());
        List<MoodLog> moodLogs = moodLogRepository.findByPatientIdOrderByTimestampDesc(patient.getId());

        // Adherence calculation
        int totalDoses = intakeLogs.size();
        int takenDoses = (int) intakeLogs.stream().filter(MedicationLog::isTaken).count();
        int missedDoses = totalDoses - takenDoses;
        double adherencePercentage = totalDoses > 0 ? (takenDoses * 100.0) / totalDoses : 0.0;

        // Risk assessment
        String riskLevel = calculateRiskLevel(adherencePercentage, moodLogs);
        String riskDescription = getRiskDescription(riskLevel);

        // Recent logs (last 10)
        List<RecentIntakeLog> recentIntakeLogs = intakeLogs.stream()
                .sorted((a, b) -> {
                    if (a.getTakenTime() == null && b.getTakenTime() == null) return 0;
                    if (a.getTakenTime() == null) return 1;
                    if (b.getTakenTime() == null) return -1;
                    return b.getTakenTime().compareTo(a.getTakenTime());
                })
                .limit(10)
                .map(log -> RecentIntakeLog.builder()
                        .medLogId(log.getMedLogId())
                        .medicationName(log.getMedication().getName())
                        .taken(log.isTaken())
                        .takenTime(log.getTakenTime())
                        .delayMinutes(log.getDelayMinutes())
                        .build())
                .toList();

        List<RecentMoodLog> recentMoodLogs = moodLogs.stream()
                .limit(10)
                .map(log -> RecentMoodLog.builder()
                        .moodId(log.getMoodId())
                        .beforeMood(log.getBeforeMood())
                        .afterMood(log.getAfterMood())
                        .symptomScore(log.getSymptomScore())
                        .timestamp(log.getTimestamp())
                        .build())
                .toList();

        // Generate alerts
        List<DashboardResponse.Alert> alerts = generateAlerts(intakeLogs, moodLogs);

        // Next Reminder
        List<Reminder> reminders = reminderRepository.findByPatientIdOrderByReminderTimeAsc(patient.getId());
        Reminder nextReminder = reminders.stream()
                .filter(Reminder::isActive)
                .filter(r -> r.getReminderTime() != null && r.getReminderTime().isAfter(java.time.LocalTime.now()))
                .findFirst()
                .orElse(null);

        if (nextReminder == null && !reminders.isEmpty()) {
            nextReminder = reminders.stream()
                .filter(Reminder::isActive)
                .filter(r -> r.getReminderTime() != null)
                .findFirst()
                .orElse(null);
        }

        String nextReminderTime = nextReminder != null ? nextReminder.getReminderTime().toString() : null;
        String nextReminderMedication = nextReminder != null ? (nextReminder.getMedication() != null ? nextReminder.getMedication().getName() : nextReminder.getMessage()) : null;

        return DashboardResponse.builder()
                .patientId(patient.getId())
                .patientName(patient.getName())
                .adherencePercentage(Math.round(adherencePercentage * 100.0) / 100.0)
                .totalDoses(totalDoses)
                .takenDoses(takenDoses)
                .missedDoses(missedDoses)
                .riskLevel(riskLevel)
                .riskDescription(riskDescription)
                .nextReminderTime(nextReminderTime)
                .nextReminderMedication(nextReminderMedication)
                .recentIntakeLogs(recentIntakeLogs)
                .recentMoodLogs(recentMoodLogs)
                .alerts(alerts)
                .build();
    }

    // ── Risk Level Calculation ─────────────────────────────────

    private String calculateRiskLevel(double adherence, List<MoodLog> moodLogs) {
        double avgSymptomScore = moodLogs.stream()
                .limit(5)
                .mapToInt(MoodLog::getSymptomScore)
                .average()
                .orElse(0.0);

        if (adherence < 50 || avgSymptomScore >= 8) return "CRITICAL";
        if (adherence < 70 || avgSymptomScore >= 6) return "HIGH";
        if (adherence < 85 || avgSymptomScore >= 4) return "MODERATE";
        return "LOW";
    }

    private String getRiskDescription(String riskLevel) {
        return switch (riskLevel) {
            case "CRITICAL" -> "Immediate attention required. Very low adherence or severe symptoms.";
            case "HIGH" -> "Significant concern. Adherence is below target or symptoms are elevated.";
            case "MODERATE" -> "Needs monitoring. Some doses missed or mild symptoms reported.";
            case "LOW" -> "On track. Good adherence and stable symptoms.";
            default -> "Unable to assess risk.";
        };
    }

    // ── Alert Generation ───────────────────────────────────────

    private List<DashboardResponse.Alert> generateAlerts(List<MedicationLog> intakeLogs, List<MoodLog> moodLogs) {
        List<DashboardResponse.Alert> alerts = new ArrayList<>();

        // Check for missed doses (recent)
        long recentMissed = intakeLogs.stream()
                .filter(log -> !log.isTaken())
                .filter(log -> log.getTakenTime() != null && log.getTakenTime().isAfter(LocalDateTime.now().minusDays(7)))
                .count();

        if (recentMissed > 0) {
            alerts.add(DashboardResponse.Alert.builder()
                    .type("MISSED_DOSE")
                    .severity(recentMissed >= 3 ? "CRITICAL" : "WARNING")
                    .message(recentMissed + " missed dose(s) in the last 7 days")
                    .timestamp(LocalDateTime.now())
                    .build());
        }

        // Check for delayed doses (delay > 30 min)
        long delayedDoses = intakeLogs.stream()
                .filter(log -> log.getDelayMinutes() != null && log.getDelayMinutes() > 30)
                .filter(log -> log.getTakenTime() != null && log.getTakenTime().isAfter(LocalDateTime.now().minusDays(7)))
                .count();

        if (delayedDoses > 0) {
            alerts.add(DashboardResponse.Alert.builder()
                    .type("DELAYED_DOSE")
                    .severity("INFO")
                    .message(delayedDoses + " dose(s) delayed by more than 30 minutes this week")
                    .timestamp(LocalDateTime.now())
                    .build());
        }

        // Check for low mood
        moodLogs.stream()
                .filter(log -> log.getTimestamp().isAfter(LocalDateTime.now().minusDays(3)))
                .filter(log -> log.getAfterMood() <= 2)
                .findFirst()
                .ifPresent(log -> alerts.add(DashboardResponse.Alert.builder()
                        .type("LOW_MOOD")
                        .severity("WARNING")
                        .message("Low mood detected (score: " + log.getAfterMood() + "/5) in the last 3 days")
                        .timestamp(log.getTimestamp())
                        .build()));

        // Check for high symptom score
        moodLogs.stream()
                .filter(log -> log.getTimestamp().isAfter(LocalDateTime.now().minusDays(3)))
                .filter(log -> log.getSymptomScore() >= 8)
                .findFirst()
                .ifPresent(log -> alerts.add(DashboardResponse.Alert.builder()
                        .type("HIGH_SYMPTOM")
                        .severity("CRITICAL")
                        .message("High symptom score (" + log.getSymptomScore() + "/10) reported in the last 3 days")
                        .timestamp(log.getTimestamp())
                        .build()));

        return alerts;
    }
}
