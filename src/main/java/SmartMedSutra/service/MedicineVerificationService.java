package SmartMedSutra.service;

import SmartMedSutra.entity.Medication;
import SmartMedSutra.entity.User;
import SmartMedSutra.entity.VerificationLog;
import SmartMedSutra.repository.MedicationRepository;
import SmartMedSutra.repository.UserRepository;
import SmartMedSutra.repository.VerificationLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicineVerificationService {

    private final VerificationLogRepository verificationLogRepository;
    private final UserRepository userRepository;
    private final MedicationRepository medicationRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    private static final String PYTHON_API_URL = "http://localhost:8000/verify-medicine";

    public VerificationLog verifyMedicine(Long patientId, Long medicationId, MultipartFile image) throws Exception {
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Medication medication = medicationRepository.findById(medicationId)
                .orElseThrow(() -> new RuntimeException("Medication not found"));

        String expectedName = medication.getName();

        // Prepare multipart request for Python API
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("expected_name", expectedName);
        body.add("image", new ByteArrayResource(image.getBytes()) {
            @Override
            public String getFilename() {
                return image.getOriginalFilename() != null ? image.getOriginalFilename() : "image.jpg";
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(PYTHON_API_URL, requestEntity, Map.class);
            Map<String, Object> responseBody = response.getBody();

            if (responseBody == null) {
                throw new RuntimeException("Empty response from AI service");
            }

            String detectedName = (String) responseBody.getOrDefault("detected_name", "Unknown");
            Double confidence = ((Number) responseBody.getOrDefault("confidence", 0.0)).doubleValue();
            String result = (String) responseBody.getOrDefault("result", "MISMATCH");

            VerificationLog logEntry = VerificationLog.builder()
                    .patient(patient)
                    .medication(medication)
                    .detectedName(detectedName)
                    .confidenceScore(confidence)
                    .result(result)
                    .timestamp(LocalDateTime.now())
                    .build();

            return verificationLogRepository.save(logEntry);
        } catch (Exception e) {
            log.error("Failed to verify medicine using AI service", e);
            throw new RuntimeException("AI Verification failed: " + e.getMessage());
        }
    }
}
