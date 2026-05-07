package SmartMedSutra.controller;

import SmartMedSutra.dto.EnvironmentRequest;
import SmartMedSutra.dto.EnvironmentResponse;
import SmartMedSutra.dto.WeatherResponse;
import SmartMedSutra.service.EnvironmentService;
import SmartMedSutra.service.WeatherService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/environment")
@RequiredArgsConstructor
@Tag(name = "Environment", description = "Environmental data tracking APIs")
public class EnvironmentController {

    private final EnvironmentService environmentService;
    private final WeatherService weatherService;

    // GET /environment/weather
    @GetMapping("/weather")
    public ResponseEntity<WeatherResponse> getCurrentWeather(@RequestParam String location) {
        WeatherResponse response = weatherService.getCurrentWeather(location);
        return ResponseEntity.ok(response);
    }

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
