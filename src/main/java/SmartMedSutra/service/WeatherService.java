package SmartMedSutra.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WebClient webClient;

    @Value("${weather.api.key}")
    private String apiKey;

    // ── Fetch Weather Data (temperature, humidity, lat, lon) ───

    public JsonNode getWeatherData(String location) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/data/2.5/weather")
                            .queryParam("q", location)
                            .queryParam("appid", apiKey)
                            .queryParam("units", "metric")
                            .build())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (Exception e) {
            log.error("Failed to fetch weather data for location: {}", location, e);
            throw new RuntimeException("Failed to fetch weather data for: " + location);
        }
    }

    // ── Fetch AQI Data ─────────────────────────────────────────

    public JsonNode getAqiData(double lat, double lon) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/data/2.5/air_pollution")
                            .queryParam("lat", lat)
                            .queryParam("lon", lon)
                            .queryParam("appid", apiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (Exception e) {
            log.error("Failed to fetch AQI data for lat: {}, lon: {}", lat, lon, e);
            throw new RuntimeException("Failed to fetch AQI data");
        }
    }

    // ── Extract temperature from weather response ──────────────

    public Double extractTemperature(JsonNode weatherData) {
        return weatherData.path("main").path("temp").asDouble();
    }

    // ── Extract humidity from weather response ─────────────────

    public Double extractHumidity(JsonNode weatherData) {
        return weatherData.path("main").path("humidity").asDouble();
    }

    // ── Extract coordinates from weather response ──────────────

    public double extractLat(JsonNode weatherData) {
        return weatherData.path("coord").path("lat").asDouble();
    }

    public double extractLon(JsonNode weatherData) {
        return weatherData.path("coord").path("lon").asDouble();
    }

    // ── Extract AQI from air pollution response ────────────────

    public Double extractAqi(JsonNode aqiData) {
        return aqiData.path("list").get(0).path("main").path("aqi").asDouble();
    }
}
