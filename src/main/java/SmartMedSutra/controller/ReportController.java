package SmartMedSutra.controller;

import SmartMedSutra.dto.AdherenceReportResponse;
import SmartMedSutra.dto.HealthReportResponse;
import SmartMedSutra.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    // GET /reports/adherence/{patientId}
    @GetMapping("/adherence/{patientId}")
    public ResponseEntity<AdherenceReportResponse> getAdherenceReport(@PathVariable Long patientId) {
        AdherenceReportResponse response = reportService.getAdherenceReport(patientId);
        return ResponseEntity.ok(response);
    }

    // GET /reports/health/{patientId}
    @GetMapping("/health/{patientId}")
    public ResponseEntity<HealthReportResponse> getHealthReport(@PathVariable Long patientId) {
        HealthReportResponse response = reportService.getHealthReport(patientId);
        return ResponseEntity.ok(response);
    }
}
