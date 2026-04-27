package SmartMedSutra.controller;

import SmartMedSutra.dto.EnvironmentRequest;
import SmartMedSutra.dto.EnvironmentResponse;
import SmartMedSutra.service.EnvironmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/environment")
@RequiredArgsConstructor
public class EnvironmentController {

    private final EnvironmentService environmentService;

    // POST /environment
    @PostMapping
    public ResponseEntity<EnvironmentResponse> fetchEnvironmentData(@Valid @RequestBody EnvironmentRequest request) {
        EnvironmentResponse response = environmentService.fetchAndSaveEnvironmentData(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /environment/{patientId}
    @GetMapping("/{patientId}")
    public ResponseEntity<List<EnvironmentResponse>> getEnvironmentData(@PathVariable Long patientId) {
        List<EnvironmentResponse> response = environmentService.getEnvironmentDataByPatientId(patientId);
        return ResponseEntity.ok(response);
    }
}
