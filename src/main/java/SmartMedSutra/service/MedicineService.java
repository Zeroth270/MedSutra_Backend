package SmartMedSutra.service;

import SmartMedSutra.dto.MedicineAlternativeResponse;
import SmartMedSutra.entity.MedicinePrice;
import SmartMedSutra.repository.MedicinePriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicineService {

    private final MedicinePriceRepository medicinePriceRepository;

    public MedicineAlternativeResponse getAlternative(String medicineName) {
        MedicinePrice medicine = medicinePriceRepository
                .findByMedicineNameIgnoreCase(medicineName.trim())
                .orElseThrow(() -> new RuntimeException(
                        "Medicine not found: " + medicineName
                        + ". Please check the name or add it to the database."));

        double savings = medicine.getBrandPrice() - medicine.getGenericPrice();
        boolean genericCheaper = savings > 0;

        String recommendation = genericCheaper
                ? String.format("Choose '%s' to save ₹%.0f per course.", medicine.getGenericName(), savings)
                : "The generic alternative offers no significant price advantage in this case.";

        return MedicineAlternativeResponse.builder()
                .medicineName(medicine.getMedicineName())
                .brandPrice(medicine.getBrandPrice())
                .genericName(medicine.getGenericName())
                .genericPrice(medicine.getGenericPrice())
                .savings(Math.max(savings, 0))
                .recommendation(recommendation)
                .build();
    }
}
