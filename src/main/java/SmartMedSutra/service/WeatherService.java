package SmartMedSutra.service;

import SmartMedSutra.dto.WeatherResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${weather.api.key}")
    private String apiKey;

    // ── Get Current Weather (Unified) ──────────────────────────

    public WeatherResponse getCurrentWeather(String location) {
        JsonNode weatherData = getWeatherData(location);
        if (weatherData == null) return null;

        double lat = extractLat(weatherData);
        double lon = extractLon(weatherData);
        JsonNode aqiData = getAqiData(lat, lon);

        return WeatherResponse.builder()
                .locationName(extractLocationName(weatherData))
                .temp(extractTemperature(weatherData))
                .feelsLike(extractFeelsLike(weatherData))
                .humidity(extractHumidity(weatherData))
                .pressure(extractPressure(weatherData))
                .weatherMain(extractWeatherMain(weatherData))
                .description(extractDescription(weatherData))
                .icon(extractIcon(weatherData))
                .windSpeed(extractWindSpeed(weatherData))
                .windDeg(extractWindDeg(weatherData))
                .visibility(extractVisibility(weatherData))
                .clouds(extractClouds(weatherData))
                .seaLevel(extractSeaLevel(weatherData))
                .groundLevel(extractGroundLevel(weatherData))
                .aqi(extractAqi(aqiData))
                .build();
    }

    // ── Fetch Weather Data (temperature, humidity, lat, lon) ───

    public JsonNode getWeatherData(String location) {
        try {
            String responseBody = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/data/2.5/weather")
                            .queryParam("q", location)
                            .queryParam("appid", apiKey)
                            .queryParam("units", "metric")
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return objectMapper.readTree(responseBody);
        } catch (Exception e) {
            log.error("Failed to fetch weather data for location: {}", location, e);
            throw new RuntimeException("Failed to fetch weather data for: " + location + ". Reason: " + e.getMessage());
        }
    }

    // ── Fetch AQI Data ─────────────────────────────────────────

    public JsonNode getAqiData(double lat, double lon) {
        try {
            String responseBody = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/data/2.5/air_pollution")
                            .queryParam("lat", lat)
                            .queryParam("lon", lon)
                            .queryParam("appid", apiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return objectMapper.readTree(responseBody);
        } catch (Exception e) {
            log.error("Failed to fetch AQI data for lat: {}, lon: {}", lat, lon, e);
            throw new RuntimeException("Failed to fetch AQI data. Reason: " + e.getMessage());
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

    public String extractLocationName(JsonNode weatherData) {
        return weatherData.path("name").asText();
    }

    public Double extractFeelsLike(JsonNode weatherData) {
        return weatherData.path("main").path("feels_like").asDouble();
    }

    public Double extractPressure(JsonNode weatherData) {
        return weatherData.path("main").path("pressure").asDouble();
    }

    public String extractWeatherMain(JsonNode weatherData) {
        return weatherData.path("weather").get(0).path("main").asText();
    }

    public String extractDescription(JsonNode weatherData) {
        return weatherData.path("weather").get(0).path("description").asText();
    }

    public String extractIcon(JsonNode weatherData) {
        return weatherData.path("weather").get(0).path("icon").asText();
    }

    public Double extractWindSpeed(JsonNode weatherData) {
        return weatherData.path("wind").path("speed").asDouble();
    }

    public Integer extractWindDeg(JsonNode weatherData) {
        return weatherData.path("wind").path("deg").asInt();
    }

    public Integer extractVisibility(JsonNode weatherData) {
        return weatherData.path("visibility").asInt();
    }

    public Integer extractClouds(JsonNode weatherData) {
        return weatherData.path("clouds").path("all").asInt();
    }

    public Double extractSeaLevel(JsonNode weatherData) {
        return weatherData.path("main").path("sea_level").asDouble();
    }

    public Double extractGroundLevel(JsonNode weatherData) {
        return weatherData.path("main").path("grnd_level").asDouble();
    }

    // ── Extract AQI from air pollution response ────────────────

    public Double extractAqi(JsonNode aqiData) {
        if (aqiData == null) return null;
        JsonNode list = aqiData.path("list");
        if (list.isEmpty()) return null;
        return list.get(0).path("main").path("aqi").asDouble();
    }
}
