package SmartMedSutra.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherResponse {
    private String locationName;
    private Double temp;
    private Double feelsLike;
    private Double humidity;
    private Double pressure;
    private String weatherMain;
    private String description;
    private String icon;
    private Double windSpeed;
    private Integer windDeg;
    private Integer visibility;
    private Integer clouds;
    private Double seaLevel;
    private Double groundLevel;
    private Double aqi;
}
