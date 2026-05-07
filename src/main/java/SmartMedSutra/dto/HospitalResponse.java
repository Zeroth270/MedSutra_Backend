package SmartMedSutra.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalResponse {

    private String name;
    private String address;
    private String city;
    private String state;
    private String phone;
    private boolean ayushmanSupported;
    private String distance;
}
