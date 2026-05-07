package SmartMedSutra.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EligibilityRequest {

    private Long userId;

    // Optional — if provided, these fields are saved to the User before evaluation
    private Long annualIncome;
    private String rationCardType;
    private Integer familySize;
    private String state;
    private String occupation;
}
