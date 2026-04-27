package SmartMedSutra.service;

import SmartMedSutra.dto.IntakeLogRequest;
import SmartMedSutra.dto.IntakeLogResponse;
import SmartMedSutra.entity.Medication;
import SmartMedSutra.entity.MedicationLog;
import SmartMedSutra.repository.MedicationLogRepository;
import SmartMedSutra.repository.MedicationRepository;
import SmartMedSutra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicationLogService {

    private final MedicationLogRepository medicationLogRepository;
    private final MedicationRepository medicationRepository;
    private final UserRepository userRepository;

    // ── Log Intake ─────────────────────────────────────────────

    public IntakeLogResponse logIntake(IntakeLogRequest request) {
        Medication medication = medicationRepository.findById(request.getMedicationId())
                .orElseThrow(() -> new RuntimeException("Medication not found with id: " + request.getMedicationId()));

        MedicationLog log = MedicationLog.builder()
                .medication(medication)
                .taken(request.getTaken())
                .takenTime(LocalDateTime.now())
                .delayMinutes(request.getDelayMinutes())
                .build();

        MedicationLog saved = medicationLogRepository.save(log);
        return mapToResponse(saved);
    }

    // ── Get Intake Logs by Patient ID ──────────────────────────

    public List<IntakeLogResponse> getIntakeLogsByPatientId(Long patientId) {
        if (!userRepository.existsById(patientId)) {
            throw new RuntimeException("Patient not found with id: " + patientId);
        }

        return medicationLogRepository.findByPatientId(patientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ── Helper ─────────────────────────────────────────────────

    private IntakeLogResponse mapToResponse(MedicationLog log) {
        return IntakeLogResponse.builder()
                .medLogId(log.getMedLogId())
                .medicationId(log.getMedication().getMedId())
                .medicationName(log.getMedication().getName())
                .taken(log.isTaken())
                .takenTime(log.getTakenTime())
                .delayMinutes(log.getDelayMinutes())
                .build();
    }
}
