package SmartMedSutra.controller;

import SmartMedSutra.dto.MoodLogRequest;
import SmartMedSutra.dto.MoodLogResponse;
import SmartMedSutra.service.MoodLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs/mood")
@RequiredArgsConstructor
public class MoodLogController {

    private final MoodLogService moodLogService;

    // POST /logs/mood
    @PostMapping
    public ResponseEntity<MoodLogResponse> logMood(@Valid @RequestBody MoodLogRequest request) {
        MoodLogResponse response = moodLogService.logMood(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /logs/mood/{patientId}
    @GetMapping("/{patientId}")
    public ResponseEntity<List<MoodLogResponse>> getMoodLogs(@PathVariable Long patientId) {
        List<MoodLogResponse> response = moodLogService.getMoodLogsByPatientId(patientId);
        return ResponseEntity.ok(response);
    }
}
