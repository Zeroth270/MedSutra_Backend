package SmartMedSutra.service;

import SmartMedSutra.dto.AlertRequest;
import SmartMedSutra.dto.AlertResponse;
import SmartMedSutra.entity.Alert;
import SmartMedSutra.entity.User;
import SmartMedSutra.repository.AlertRepository;
import SmartMedSutra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;
    private final UserRepository userRepository;

    // ── Send Alert ─────────────────────────────────────────────

    public AlertResponse sendAlert(AlertRequest request) {
        User patient = userRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + request.getPatientId()));

        Alert alert = Alert.builder()
                .patient(patient)
                .message(request.getMessage())
                .urgency(request.getUrgency())
                .build();

        Alert saved = alertRepository.save(alert);
        return mapToResponse(saved);
    }

    // ── Get Alerts by Patient ID ───────────────────────────────

    public List<AlertResponse> getAlertsByPatientId(Long patientId) {
        if (!userRepository.existsById(patientId)) {
            throw new RuntimeException("Patient not found with id: " + patientId);
        }

        return alertRepository.findByPatientIdOrderByCreatedAtDesc(patientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ── Helper ─────────────────────────────────────────────────

    private AlertResponse mapToResponse(Alert alert) {
        return AlertResponse.builder()
                .id(alert.getId())
                .patientId(alert.getPatient().getId())
                .patientName(alert.getPatient().getName())
                .message(alert.getMessage())
                .urgency(alert.getUrgency())
                .status(alert.getStatus())
                .createdAt(alert.getCreatedAt())
                .build();
    }
}
