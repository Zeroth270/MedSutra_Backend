package SmartMedSutra.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EligibilityResponse {

    private boolean eligible;
    private String scheme;
    private String message;
}
