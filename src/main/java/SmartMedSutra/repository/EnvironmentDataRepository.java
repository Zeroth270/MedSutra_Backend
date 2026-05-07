package SmartMedSutra.repository;

import SmartMedSutra.entity.EnvironmentData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnvironmentDataRepository extends JpaRepository<EnvironmentData, Long> {

    List<EnvironmentData> findByPatient_IdOrderByTimestampDesc(Long patientId);
}
