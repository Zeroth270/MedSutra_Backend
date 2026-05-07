package SmartMedSutra.repository;

import SmartMedSutra.entity.MedicinePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicinePriceRepository extends JpaRepository<MedicinePrice, Long> {

    Optional<MedicinePrice> findByMedicineNameIgnoreCase(String medicineName);
}
