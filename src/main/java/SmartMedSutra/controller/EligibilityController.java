package SmartMedSutra.controller;

import SmartMedSutra.dto.EligibilityRequest;
import SmartMedSutra.dto.EligibilityResponse;
import SmartMedSutra.service.EligibilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/eligibility")
@RequiredArgsConstructor
@Tag(name = "Ayushman Eligibility", description = "Check Ayushman Bharat PM-JAY eligibility for a user")
public class EligibilityController {

    private final EligibilityService eligibilityService;

    @PostMapping("/check")
    @Operation(
        summary = "Check eligibility",
        description = "Returns whether a user qualifies for Ayushman Bharat. "
            + "Optionally pass annualIncome, rationCardType, familySize, state, occupation — "
            + "these will be saved to the user profile before evaluation."
    )
    public ResponseEntity<EligibilityResponse> checkEligibility(
            @RequestBody EligibilityRequest request) {

        if (request.getUserId() == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(eligibilityService.checkEligibility(request));
    }
}
