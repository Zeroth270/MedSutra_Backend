package SmartMedSutra.service;

import SmartMedSutra.dto.CareTeamRequest;
import SmartMedSutra.dto.CareTeamResponse;
import SmartMedSutra.entity.PatientRelationship;
import SmartMedSutra.entity.User;
import SmartMedSutra.repository.PatientRelationshipRepository;
import SmartMedSutra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CareTeamService {

    private final PatientRelationshipRepository relationshipRepository;
    private final UserRepository userRepository;

    // ── Add Care Team Member ───────────────────────────────────

    public CareTeamResponse addCareTeamMember(CareTeamRequest request) {

        User patient = userRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + request.getPatientId()));

        PatientRelationship relationship = PatientRelationship.builder()
                .patient(patient)
                .relationshipType(request.getRelationshipType())
                .build();

        // Set caregiver if provided
        if (request.getCaregiverId() != null) {
            User caregiver = userRepository.findById(request.getCaregiverId())
                    .orElseThrow(() -> new RuntimeException("Caregiver not found with id: " + request.getCaregiverId()));
            relationship.setCaregiver(caregiver);
        }

        // Set doctor if provided
        if (request.getDoctorId() != null) {
            User doctor = userRepository.findById(request.getDoctorId())
                    .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + request.getDoctorId()));
            relationship.setDoctor(doctor);
        }

        PatientRelationship saved = relationshipRepository.save(relationship);
        return mapToResponse(saved);
    }

    // ── Get Care Team by Patient ID ────────────────────────────

    public List<CareTeamResponse> getCareTeamByPatientId(Long patientId) {
        if (!userRepository.existsById(patientId)) {
            throw new RuntimeException("Patient not found with id: " + patientId);
        }

        return relationshipRepository.findByPatientId(patientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ── Delete Care Team Member ────────────────────────────────

    public void deleteCareTeamMember(Long id) {
        if (!relationshipRepository.existsById(id)) {
            throw new RuntimeException("Care team relationship not found with id: " + id);
        }
        relationshipRepository.deleteById(id);
    }

    // ── Helper ─────────────────────────────────────────────────

    private CareTeamResponse mapToResponse(PatientRelationship rel) {
        return CareTeamResponse.builder()
                .id(rel.getId())
                .patientId(rel.getPatient().getId())
                .patientName(rel.getPatient().getName())
                .caregiverId(rel.getCaregiver() != null ? rel.getCaregiver().getId() : null)
                .caregiverName(rel.getCaregiver() != null ? rel.getCaregiver().getName() : null)
                .caregiverEmail(rel.getCaregiver() != null ? rel.getCaregiver().getEmail() : null)
                .doctorId(rel.getDoctor() != null ? rel.getDoctor().getId() : null)
                .doctorName(rel.getDoctor() != null ? rel.getDoctor().getName() : null)
                .doctorEmail(rel.getDoctor() != null ? rel.getDoctor().getEmail() : null)
                .relationshipType(rel.getRelationshipType())
                .build();
    }
}
