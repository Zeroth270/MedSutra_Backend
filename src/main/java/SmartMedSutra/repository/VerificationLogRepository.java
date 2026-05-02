package SmartMedSutra.repository;

import SmartMedSutra.entity.VerificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VerificationLogRepository extends JpaRepository<VerificationLog, Long> {
    List<VerificationLog> findByPatientId(Long patientId);
}
