package SmartMedSutra.service;

import SmartMedSutra.dto.EligibilityRequest;
import SmartMedSutra.dto.EligibilityResponse;
import SmartMedSutra.entity.User;
import SmartMedSutra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EligibilityService {

    private final UserRepository userRepository;

    private static final long MAX_ANNUAL_INCOME = 250_000L;
    private static final int  MIN_FAMILY_SIZE   = 2;

    /**
     * If the request includes eligibility fields (annualIncome, rationCardType, familySize),
     * they are saved to the User entity first, then eligibility is evaluated.
     * This lets callers test in one shot without a separate "update user" step.
     */
    public EligibilityResponse checkEligibility(EligibilityRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException(
                        "User not found with id: " + request.getUserId()));

        // Merge any inline fields from the request into the user record
        boolean updated = false;
        if (request.getAnnualIncome() != null) { user.setAnnualIncome(request.getAnnualIncome()); updated = true; }
        if (request.getRationCardType() != null && !request.getRationCardType().isBlank()) { user.setRationCardType(request.getRationCardType()); updated = true; }
        if (request.getFamilySize() != null)  { user.setFamilySize(request.getFamilySize());   updated = true; }
        if (request.getState() != null && !request.getState().isBlank()) { user.setState(request.getState()); updated = true; }
        if (request.getOccupation() != null && !request.getOccupation().isBlank()) { user.setOccupation(request.getOccupation()); updated = true; }

        if (updated) {
            user = userRepository.save(user);
        }

        // ── Rule-based eligibility evaluation ─────────────────────────────────
        boolean incomeOk     = user.getAnnualIncome()   != null && user.getAnnualIncome() < MAX_ANNUAL_INCOME;
        boolean rationCardOk = user.getRationCardType() != null && !user.getRationCardType().isBlank();
        boolean familySizeOk = user.getFamilySize()     != null && user.getFamilySize()   >= MIN_FAMILY_SIZE;

        boolean eligible = incomeOk && rationCardOk && familySizeOk;

        if (eligible) {
            return EligibilityResponse.builder()
                    .eligible(true)
                    .scheme("Ayushman Bharat – Pradhan Mantri Jan Arogya Yojana (PM-JAY)")
                    .message("User may qualify for healthcare benefits worth up to ₹5 lakh per year.")
                    .build();
        }

        StringBuilder reason = new StringBuilder("User does not qualify. Reasons: ");
        if (!incomeOk)     reason.append("Annual income must be below ₹2,50,000. ");
        if (!rationCardOk) reason.append("A valid ration card is required. ");
        if (!familySizeOk) reason.append("Family size must be at least 2. ");

        return EligibilityResponse.builder()
                .eligible(false)
                .scheme("Ayushman Bharat – PM-JAY")
                .message(reason.toString().trim())
                .build();
    }
}
