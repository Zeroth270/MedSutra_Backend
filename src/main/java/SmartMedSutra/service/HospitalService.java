package SmartMedSutra.service;

import SmartMedSutra.dto.HospitalResponse;
import SmartMedSutra.entity.Hospital;
import SmartMedSutra.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    // Earth's radius in kilometres
    private static final double EARTH_RADIUS_KM = 6371.0;

    /**
     * Returns nearby hospitals sorted by distance from the given coordinates.
     * Filters by city when provided; otherwise returns all hospitals.
     */
    public List<HospitalResponse> findNearbyHospitals(Double latitude, Double longitude, String city) {
        List<Hospital> hospitals;

        if (city != null && !city.isBlank()) {
            hospitals = hospitalRepository.findByCityIgnoreCase(city.trim());
        } else {
            hospitals = hospitalRepository.findAll();
        }

        return hospitals.stream()
                .map(h -> toResponse(h, latitude, longitude))
                .sorted((a, b) -> {
                    double dA = parseDistance(a.getDistance());
                    double dB = parseDistance(b.getDistance());
                    return Double.compare(dA, dB);
                })
                .collect(Collectors.toList());
    }

    // ── Haversine formula ──────────────────────────────────────────────────────

    private double haversineKm(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                 + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                 * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    private HospitalResponse toResponse(Hospital h, Double userLat, Double userLon) {
        String distance = "N/A";
        if (userLat != null && userLon != null && h.getLatitude() != null && h.getLongitude() != null) {
            double km = haversineKm(userLat, userLon, h.getLatitude(), h.getLongitude());
            distance = String.format("%.1f km", km);
        }

        return HospitalResponse.builder()
                .name(h.getName())
                .address(h.getAddress())
                .city(h.getCity())
                .state(h.getState())
                .phone(h.getPhone())
                .ayushmanSupported(h.isAyushmanSupported())
                .distance(distance)
                .build();
    }

    private double parseDistance(String distStr) {
        if ("N/A".equals(distStr)) return Double.MAX_VALUE;
        return Double.parseDouble(distStr.replace(" km", ""));
    }
}
