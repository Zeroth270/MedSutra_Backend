package SmartMedSutra.controller;

import SmartMedSutra.dto.CaregiverDashboardResponse;
import SmartMedSutra.dto.DashboardResponse;
import SmartMedSutra.dto.DoctorDashboardResponse;
import SmartMedSutra.service.DashboardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Dashboard", description = "Dashboard data APIs for patients, caregivers, and doctors")
public class DashboardController {

    private final DashboardService dashboardService;

    // GET /dashboard/patient/{id}
    @GetMapping("/patient/{id}")
    public ResponseEntity<DashboardResponse> getPatientDashboard(@PathVariable Long id) {
        DashboardResponse response = dashboardService.getPatientDashboard(id);
        return ResponseEntity.ok(response);
    }

    // GET /dashboard/caregiver/{id}
    @GetMapping("/caregiver/{id}")
    public ResponseEntity<CaregiverDashboardResponse> getCaregiverDashboard(@PathVariable Long id) {
        CaregiverDashboardResponse response = dashboardService.getCaregiverDashboard(id);
        return ResponseEntity.ok(response);
    }

    // GET /dashboard/doctor/{id}
    @GetMapping("/doctor/{id}")
    public ResponseEntity<DoctorDashboardResponse> getDoctorDashboard(@PathVariable Long id) {
        DoctorDashboardResponse response = dashboardService.getDoctorDashboard(id);
        return ResponseEntity.ok(response);
    }

    // GET /dashboard/patients/all
    @GetMapping("/patients/all")
    public ResponseEntity<List<DashboardResponse>> getAllPatientsDashboard() {
        List<DashboardResponse> response = dashboardService.getAllPatientsDashboard();
        return ResponseEntity.ok(response);
    }
}
