package SmartMedSutra.repository;

import SmartMedSutra.entity.MoodLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoodLogRepository extends JpaRepository<MoodLog, Long> {

    List<MoodLog> findByPatientIdOrderByTimestampDesc(Long patientId);
}
