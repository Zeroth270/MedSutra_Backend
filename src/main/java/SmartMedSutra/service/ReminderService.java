package SmartMedSutra.service;

import SmartMedSutra.dto.ReminderRequest;
import SmartMedSutra.dto.ReminderResponse;
import SmartMedSutra.entity.Medication;
import SmartMedSutra.entity.Reminder;
import SmartMedSutra.entity.User;
import SmartMedSutra.repository.MedicationRepository;
import SmartMedSutra.repository.ReminderRepository;
import SmartMedSutra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final UserRepository userRepository;
    private final MedicationRepository medicationRepository;

    // ── Create Reminder ────────────────────────────────────────

    public ReminderResponse createReminder(ReminderRequest request) {
        User patient = userRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + request.getPatientId()));

        Medication medication = null;
        if (request.getMedicationId() != null) {
            medication = medicationRepository.findById(request.getMedicationId())
                    .orElseThrow(() -> new RuntimeException("Medication not found with id: " + request.getMedicationId()));
        }

        Reminder reminder = Reminder.builder()
                .patient(patient)
                .medication(medication)
                .message(request.getMessage())
                .reminderTime(request.getReminderTime())
                .frequency(request.getFrequency())
                .build();

        Reminder saved = reminderRepository.save(reminder);
        return mapToResponse(saved);
    }

    // ── Get Reminders by Patient ID ────────────────────────────

    public List<ReminderResponse> getRemindersByPatientId(Long patientId) {
        if (!userRepository.existsById(patientId)) {
            throw new RuntimeException("Patient not found with id: " + patientId);
        }

        return reminderRepository.findByPatientIdOrderByReminderTimeAsc(patientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ── Helper ─────────────────────────────────────────────────

    private ReminderResponse mapToResponse(Reminder reminder) {
        return ReminderResponse.builder()
                .id(reminder.getId())
                .patientId(reminder.getPatient().getId())
                .patientName(reminder.getPatient().getName())
                .medicationId(reminder.getMedication() != null ? reminder.getMedication().getMedId() : null)
                .medicationName(reminder.getMedication() != null ? reminder.getMedication().getName() : null)
                .message(reminder.getMessage())
                .reminderTime(reminder.getReminderTime())
                .frequency(reminder.getFrequency())
                .active(reminder.isActive())
                .createdAt(reminder.getCreatedAt())
                .build();
    }
}
