package SmartMedSutra.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineAlternativeResponse {

    private String medicineName;
    private Double brandPrice;
    private String genericName;
    private Double genericPrice;
    private Double savings;
    private String recommendation;
}
