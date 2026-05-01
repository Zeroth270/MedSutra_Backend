package SmartMedSutra.controller;

import SmartMedSutra.dto.ReminderRequest;
import SmartMedSutra.dto.ReminderResponse;
import SmartMedSutra.service.ReminderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reminder")
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderService reminderService;

    // POST /reminder/create
    @PostMapping("/create")
    public ResponseEntity<ReminderResponse> createReminder(@Valid @RequestBody ReminderRequest request) {
        ReminderResponse response = reminderService.createReminder(request);
        return ResponseEntity.ok(response);
    }

    // GET /reminder/{patientId}
    @GetMapping("/{patientId}")
    public ResponseEntity<List<ReminderResponse>> getRemindersByPatientId(@PathVariable Long patientId) {
        List<ReminderResponse> responses = reminderService.getRemindersByPatientId(patientId);
        return ResponseEntity.ok(responses);
    }
}
