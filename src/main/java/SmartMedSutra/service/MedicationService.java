package SmartMedSutra.service;

import SmartMedSutra.dto.MedicationRequest;
import SmartMedSutra.dto.MedicationResponse;
import SmartMedSutra.entity.Medication;
import SmartMedSutra.entity.User;
import SmartMedSutra.repository.MedicationRepository;
import SmartMedSutra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicationService {

    private final MedicationRepository medicationRepository;
    private final UserRepository userRepository;

    // ── Create Medication ──────────────────────────────────────

    public MedicationResponse createMedication(MedicationRequest request) {
        User patient = userRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + request.getPatientId()));

        Medication medication = Medication.builder()
                .patient(patient)
                .name(request.getName())
                .dosage(request.getDosage())
                .time(request.getTime())
                .frequency(request.getFrequency())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        Medication saved = medicationRepository.save(medication);
        return mapToResponse(saved);
    }

    // ── Get Medications by Patient ID ──────────────────────────

    public List<MedicationResponse> getMedicationsByPatientId(Long patientId) {
        if (!userRepository.existsById(patientId)) {
            throw new RuntimeException("Patient not found with id: " + patientId);
        }

        return medicationRepository.findByPatientId(patientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ── Update Medication ──────────────────────────────────────

    public MedicationResponse updateMedication(Long id, MedicationRequest request) {
        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medication not found with id: " + id));

        medication.setName(request.getName());
        medication.setDosage(request.getDosage());
        medication.setTime(request.getTime());
        medication.setFrequency(request.getFrequency());
        medication.setStartDate(request.getStartDate());
        medication.setEndDate(request.getEndDate());

        Medication updated = medicationRepository.save(medication);
        return mapToResponse(updated);
    }

    // ── Delete Medication ──────────────────────────────────────

    public void deleteMedication(Long id) {
        if (!medicationRepository.existsById(id)) {
            throw new RuntimeException("Medication not found with id: " + id);
        }
        medicationRepository.deleteById(id);
    }

    // ── Helper ─────────────────────────────────────────────────

    private MedicationResponse mapToResponse(Medication med) {
        return MedicationResponse.builder()
                .medId(med.getMedId())
                .patientId(med.getPatient().getId())
                .patientName(med.getPatient().getName())
                .name(med.getName())
                .dosage(med.getDosage())
                .time(med.getTime())
                .frequency(med.getFrequency())
                .startDate(med.getStartDate())
                .endDate(med.getEndDate())
                .build();
    }
}
