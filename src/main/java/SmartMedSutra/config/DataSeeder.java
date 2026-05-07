package SmartMedSutra.config;

import SmartMedSutra.entity.Hospital;
import SmartMedSutra.entity.MedicinePrice;
import SmartMedSutra.entity.GovernmentScheme;
import SmartMedSutra.repository.HospitalRepository;
import SmartMedSutra.repository.MedicinePriceRepository;
import SmartMedSutra.repository.GovernmentSchemeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Seeds sample Hospitals, MedicinePrices, and GovernmentSchemes on first startup.
 * Data is only inserted when the table is empty, so re-runs are safe.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final HospitalRepository hospitalRepository;
    private final MedicinePriceRepository medicinePriceRepository;
    private final GovernmentSchemeRepository governmentSchemeRepository;

    @Override
    public void run(String... args) {
        seedHospitals();
        seedMedicines();
        seedSchemes();
    }

    // ── Hospitals ──────────────────────────────────────────────────────────────

    private void seedHospitals() {
        if (hospitalRepository.count() > 0) {
            log.info("[DataSeeder] Hospitals already seeded — skipping.");
            return;
        }
        List<Hospital> hospitals = List.of(
            Hospital.builder().name("AIIMS Delhi").address("Ansari Nagar East, New Delhi").city("Delhi").state("Delhi")
                .latitude(28.5672).longitude(77.2100).phone("011-26588500").ayushmanSupported(true).build(),
            Hospital.builder().name("Safdarjung Hospital").address("Ansari Nagar West, New Delhi").city("Delhi").state("Delhi")
                .latitude(28.5688).longitude(77.2064).phone("011-26165060").ayushmanSupported(true).build(),
            Hospital.builder().name("Ram Manohar Lohia Hospital").address("Baba Kharak Singh Marg, New Delhi").city("Delhi").state("Delhi")
                .latitude(28.6268).longitude(77.2097).phone("011-23365525").ayushmanSupported(true).build(),
            Hospital.builder().name("City Care Hospital").address("MG Road, Pune").city("Pune").state("Maharashtra")
                .latitude(18.5204).longitude(73.8567).phone("9876543210").ayushmanSupported(true).build(),
            Hospital.builder().name("Deenanath Mangeshkar Hospital").address("Erandwane, Pune").city("Pune").state("Maharashtra")
                .latitude(18.5112).longitude(73.8266).phone("020-49156789").ayushmanSupported(false).build(),
            Hospital.builder().name("Rajiv Gandhi Government General Hospital").address("Park Town, Chennai").city("Chennai").state("Tamil Nadu")
                .latitude(13.0827).longitude(80.2707).phone("044-25305100").ayushmanSupported(true).build(),
            Hospital.builder().name("Government Medical College Hospital").address("Medical College Road, Thrissur").city("Thrissur").state("Kerala")
                .latitude(10.5276).longitude(76.2144).phone("0487-2361240").ayushmanSupported(true).build(),
            Hospital.builder().name("Victoria Hospital").address("Fort, Bengaluru").city("Bengaluru").state("Karnataka")
                .latitude(12.9653).longitude(77.5685).phone("080-26704444").ayushmanSupported(true).build(),
            Hospital.builder().name("Kolkata Medical College").address("College Street, Kolkata").city("Kolkata").state("West Bengal")
                .latitude(22.5726).longitude(88.3639).phone("033-22551060").ayushmanSupported(true).build(),
            Hospital.builder().name("Jawaharlal Nehru Hospital").address("Bhilai, Chhattisgarh").city("Bhilai").state("Chhattisgarh")
                .latitude(21.1938).longitude(81.3509).phone("0788-2228383").ayushmanSupported(true).build()
        );
        hospitalRepository.saveAll(hospitals);
        log.info("[DataSeeder] Seeded {} hospitals.", hospitals.size());
    }

    // ── Medicine Prices ────────────────────────────────────────────────────────

    private void seedMedicines() {
        if (medicinePriceRepository.count() > 0) {
            log.info("[DataSeeder] Medicine prices already seeded — skipping.");
            return;
        }
        List<MedicinePrice> medicines = List.of(
            MedicinePrice.builder().medicineName("Paracetamol").brandPrice(120.0).genericName("Paracetamol Generic").genericPrice(35.0).manufacturer("Jan Aushadhi").build(),
            MedicinePrice.builder().medicineName("Metformin").brandPrice(180.0).genericName("Metformin Generic").genericPrice(45.0).manufacturer("Jan Aushadhi").build(),
            MedicinePrice.builder().medicineName("Atorvastatin").brandPrice(350.0).genericName("Atorvastatin Generic").genericPrice(80.0).manufacturer("Jan Aushadhi").build(),
            MedicinePrice.builder().medicineName("Amlodipine").brandPrice(210.0).genericName("Amlodipine Generic").genericPrice(55.0).manufacturer("Jan Aushadhi").build(),
            MedicinePrice.builder().medicineName("Omeprazole").brandPrice(160.0).genericName("Omeprazole Generic").genericPrice(40.0).manufacturer("Jan Aushadhi").build(),
            MedicinePrice.builder().medicineName("Amoxicillin").brandPrice(280.0).genericName("Amoxicillin Generic").genericPrice(75.0).manufacturer("Jan Aushadhi").build(),
            MedicinePrice.builder().medicineName("Ciprofloxacin").brandPrice(320.0).genericName("Ciprofloxacin Generic").genericPrice(90.0).manufacturer("Jan Aushadhi").build(),
            MedicinePrice.builder().medicineName("Losartan").brandPrice(250.0).genericName("Losartan Generic").genericPrice(60.0).manufacturer("Jan Aushadhi").build(),
            MedicinePrice.builder().medicineName("Pantoprazole").brandPrice(190.0).genericName("Pantoprazole Generic").genericPrice(50.0).manufacturer("Jan Aushadhi").build(),
            MedicinePrice.builder().medicineName("Azithromycin").brandPrice(400.0).genericName("Azithromycin Generic").genericPrice(110.0).manufacturer("Jan Aushadhi").build(),
            MedicinePrice.builder().medicineName("Ibuprofen").brandPrice(140.0).genericName("Ibuprofen Generic").genericPrice(30.0).manufacturer("Jan Aushadhi").build(),
            MedicinePrice.builder().medicineName("Cetirizine").brandPrice(95.0).genericName("Cetirizine Generic").genericPrice(20.0).manufacturer("Jan Aushadhi").build()
        );
        medicinePriceRepository.saveAll(medicines);
        log.info("[DataSeeder] Seeded {} medicine prices.", medicines.size());
    }

    // ── Government Schemes ─────────────────────────────────────────────────────

    private void seedSchemes() {
        if (governmentSchemeRepository.count() > 0) {
            log.info("[DataSeeder] Government schemes already seeded — skipping.");
            return;
        }
        List<GovernmentScheme> schemes = List.of(
            GovernmentScheme.builder()
                .schemeName("Ayushman Bharat – PM-JAY")
                .eligibilityCriteria("Annual income < ₹2,50,000; valid ration card; family size ≥ 2")
                .benefits("Up to ₹5 lakh health coverage per family per year at empanelled hospitals")
                .state("All India")
                .build(),
            GovernmentScheme.builder()
                .schemeName("Pradhan Mantri Surakshit Matritva Abhiyan")
                .eligibilityCriteria("Pregnant women in second/third trimester")
                .benefits("Free antenatal care on the 9th of every month")
                .state("All India")
                .build(),
            GovernmentScheme.builder()
                .schemeName("Mahatma Jyotiba Phule Jan Arogya Yojana")
                .eligibilityCriteria("Below poverty line, yellow/orange ration card holders in Maharashtra")
                .benefits("Up to ₹1.5 lakh medical coverage across 996 procedures")
                .state("Maharashtra")
                .build(),
            GovernmentScheme.builder()
                .schemeName("Chief Minister's Comprehensive Health Insurance Scheme")
                .eligibilityCriteria("Annual family income below ₹72,000 in Tamil Nadu")
                .benefits("Up to ₹5 lakh for 1,027 procedures at government and empanelled private hospitals")
                .state("Tamil Nadu")
                .build()
        );
        governmentSchemeRepository.saveAll(schemes);
        log.info("[DataSeeder] Seeded {} government schemes.", schemes.size());
    }
}
