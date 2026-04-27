package SmartMedSutra.controller;

import SmartMedSutra.dto.MedicationRequest;
import SmartMedSutra.dto.MedicationResponse;
import SmartMedSutra.service.MedicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medications")
@RequiredArgsConstructor
public class MedicationController {

    private final MedicationService medicationService;

    // POST /medications
    @PostMapping
    public ResponseEntity<MedicationResponse> createMedication(@Valid @RequestBody MedicationRequest request) {
        MedicationResponse response = medicationService.createMedication(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /medications/{patientId}
    @GetMapping("/{patientId}")
    public ResponseEntity<List<MedicationResponse>> getMedications(@PathVariable Long patientId) {
        List<MedicationResponse> response = medicationService.getMedicationsByPatientId(patientId);
        return ResponseEntity.ok(response);
    }

    // PUT /medications/{id}
    @PutMapping("/{id}")
    public ResponseEntity<MedicationResponse> updateMedication(@PathVariable Long id,
                                                               @Valid @RequestBody MedicationRequest request) {
        MedicationResponse response = medicationService.updateMedication(id, request);
        return ResponseEntity.ok(response);
    }

    // DELETE /medications/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMedication(@PathVariable Long id) {
        medicationService.deleteMedication(id);
        return ResponseEntity.ok("Medication deleted successfully");
    }
}
