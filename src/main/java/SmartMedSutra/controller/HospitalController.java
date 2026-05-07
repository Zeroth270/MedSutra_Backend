package SmartMedSutra.controller;

import SmartMedSutra.dto.HospitalResponse;
import SmartMedSutra.service.HospitalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hospitals")
@RequiredArgsConstructor
@Tag(name = "Nearby Hospitals", description = "Find Ayushman-supported hospitals near a location")
public class HospitalController {

    private final HospitalService hospitalService;

    @GetMapping("/nearby")
    @Operation(
        summary = "Get nearby hospitals",
        description = "Returns hospitals near the given coordinates (Haversine distance), optionally filtered by city."
    )
    public ResponseEntity<List<HospitalResponse>> getNearbyHospitals(
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) String city) {

        List<HospitalResponse> hospitals = hospitalService.findNearbyHospitals(latitude, longitude, city);
        return ResponseEntity.ok(hospitals);
    }
}
