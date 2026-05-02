package SmartMedSutra.repository;

import SmartMedSutra.entity.MedicineReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicineReferenceRepository extends JpaRepository<MedicineReference, Long> {
    Optional<MedicineReference> findByNameIgnoreCase(String name);
}
