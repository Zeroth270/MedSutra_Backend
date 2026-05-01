package SmartMedSutra.controller;

import SmartMedSutra.dto.AlertRequest;
import SmartMedSutra.dto.AlertResponse;
import SmartMedSutra.service.AlertService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alerts")
@RequiredArgsConstructor
@Tag(name = "Alerts", description = "Alert notification management APIs")
@CrossOrigin("*")
public class AlertController {

    private final AlertService alertService;

    // POST /alerts/send
    @PostMapping("/send")
    public ResponseEntity<AlertResponse> sendAlert(@Valid @RequestBody AlertRequest request) {
        AlertResponse response = alertService.sendAlert(request);
        return ResponseEntity.ok(response);
    }

    // GET /alerts/{patientId}
    @GetMapping("/{patientId}")
    public ResponseEntity<List<AlertResponse>> getAlertsByPatientId(@PathVariable Long patientId) {
        List<AlertResponse> responses = alertService.getAlertsByPatientId(patientId);
        return ResponseEntity.ok(responses);
    }
}
