package SmartMedSutra.controller;

import SmartMedSutra.dto.CareTeamRequest;
import SmartMedSutra.dto.CareTeamResponse;
import SmartMedSutra.service.CareTeamService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/care-team")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Care Team", description = "Care team member management APIs")
public class CareTeamController {

    private final CareTeamService careTeamService;

    // POST /care-team/add
    @PostMapping("/add")
    public ResponseEntity<CareTeamResponse> addCareTeamMember(@Valid @RequestBody CareTeamRequest request) {
        CareTeamResponse response = careTeamService.addCareTeamMember(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /care-team/{patientId}
    @GetMapping("/{patientId}")
    public ResponseEntity<List<CareTeamResponse>> getCareTeam(@PathVariable Long patientId) {
        List<CareTeamResponse> response = careTeamService.getCareTeamByPatientId(patientId);
        return ResponseEntity.ok(response);
    }

    // DELETE /care-team/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCareTeamMember(@PathVariable Long id) {
        careTeamService.deleteCareTeamMember(id);
        return ResponseEntity.ok("Care team member removed successfully");
    }
}
