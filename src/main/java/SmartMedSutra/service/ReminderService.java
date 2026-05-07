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

    // ── Update Reminder ────────────────────────────────────────

    public ReminderResponse updateReminder(Long id, ReminderRequest request) {
        Reminder existingReminder = reminderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reminder not found with id: " + id));

        // Note: we generally don't change the patient of an existing reminder.
        // We verify the patient ID matches, or at least we fetch the proper medication if updated.
        
        Medication medication = existingReminder.getMedication();
        if (request.getMedicationId() != null && 
            (medication == null || !medication.getMedId().equals(request.getMedicationId()))) {
            medication = medicationRepository.findById(request.getMedicationId())
                    .orElseThrow(() -> new RuntimeException("Medication not found with id: " + request.getMedicationId()));
        } else if (request.getMedicationId() == null) {
            medication = null;
        }

        existingReminder.setMedication(medication);
        existingReminder.setMessage(request.getMessage());
        existingReminder.setReminderTime(request.getReminderTime());
        
        if (request.getFrequency() != null) {
            existingReminder.setFrequency(request.getFrequency());
        }
        if (request.getActive() != null) {
            existingReminder.setActive(request.getActive());
        }

        Reminder updated = reminderRepository.save(existingReminder);
        return mapToResponse(updated);
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

    // ── Delete Reminder ────────────────────────────────────────

    public void deleteReminder(Long id) {
        if (!reminderRepository.existsById(id)) {
            throw new RuntimeException("Reminder not found with id: " + id);
        }
        reminderRepository.deleteById(id);
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
