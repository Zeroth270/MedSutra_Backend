package SmartMedSutra.service;

import SmartMedSutra.dto.EnvironmentRequest;
import SmartMedSutra.dto.EnvironmentResponse;
import SmartMedSutra.entity.EnvironmentData;
import SmartMedSutra.entity.User;
import SmartMedSutra.repository.EnvironmentDataRepository;
import SmartMedSutra.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnvironmentService {

    private final EnvironmentDataRepository environmentDataRepository;
    private final UserRepository userRepository;
    private final WeatherService weatherService;

    // ── Fetch & Save Environment Data ──────────────────────────

    public EnvironmentResponse fetchAndSaveEnvironmentData(EnvironmentRequest request) {
        User patient = userRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + request.getPatientId()));

        // Fetch weather data from OpenWeatherMap
        JsonNode weatherData = weatherService.getWeatherData(request.getLocation());
        Double temperature = weatherService.extractTemperature(weatherData);
        Double humidity = weatherService.extractHumidity(weatherData);

        // Fetch AQI using coordinates — wrapped in try/catch so a bad AQI response
        // does not crash the POST request; AQI will be null instead.
        Double aqi = null;
        try {
            double lat = weatherService.extractLat(weatherData);
            double lon = weatherService.extractLon(weatherData);
            JsonNode aqiData = weatherService.getAqiData(lat, lon);
            if (aqiData != null) {
                aqi = weatherService.extractAqi(aqiData);
            }
        } catch (Exception e) {
            // AQI fetch failed — save record with null AQI rather than failing the request
        }

        // Save to database
        EnvironmentData envData = EnvironmentData.builder()
                .patient(patient)
                .aqi(aqi)
                .temperature(temperature)
                .humidity(humidity)
                .location(request.getLocation())
                .build();

        EnvironmentData saved = environmentDataRepository.save(envData);
        return mapToResponse(saved);
    }

    // ── Get Environment Data by Patient ID ─────────────────────

    public List<EnvironmentResponse> getEnvironmentDataByPatientId(Long patientId) {
        if (!userRepository.existsById(patientId)) {
            throw new RuntimeException("Patient not found with id: " + patientId);
        }

        return environmentDataRepository.findByPatient_IdOrderByTimestampDesc(patientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ── Helper ─────────────────────────────────────────────────

    private EnvironmentResponse mapToResponse(EnvironmentData env) {
        return EnvironmentResponse.builder()
                .environmentId(env.getEnvironmentId())
                .patientId(env.getPatient().getId())
                .patientName(env.getPatient().getName())
                .aqi(env.getAqi())
                .temperature(env.getTemperature())
                .humidity(env.getHumidity())
                .location(env.getLocation())
                .timestamp(env.getTimestamp())
                .build();
    }
}
