package SmartMedSutra.controller;

import SmartMedSutra.dto.IntakeLogRequest;
import SmartMedSutra.dto.IntakeLogResponse;
import SmartMedSutra.service.MedicationLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs/intake")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Medication Logs", description = "Medication intake logging APIs")
public class MedicationLogController {

    private final MedicationLogService medicationLogService;

    // POST /logs/intake
    @PostMapping
    public ResponseEntity<IntakeLogResponse> logIntake(@Valid @RequestBody IntakeLogRequest request) {
        IntakeLogResponse response = medicationLogService.logIntake(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /logs/intake/{patientId}
    @GetMapping("/{patientId}")
    public ResponseEntity<List<IntakeLogResponse>> getIntakeLogs(@PathVariable Long patientId) {
        List<IntakeLogResponse> response = medicationLogService.getIntakeLogsByPatientId(patientId);
        return ResponseEntity.ok(response);
    }
}
