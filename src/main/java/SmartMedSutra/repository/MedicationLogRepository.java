package SmartMedSutra.repository;

import SmartMedSutra.entity.MedicationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicationLogRepository extends JpaRepository<MedicationLog, Long> {

    @Query("SELECT ml FROM MedicationLog ml WHERE ml.medication.patient.id = :patientId ORDER BY ml.takenTime DESC")
    List<MedicationLog> findByPatientId(@Param("patientId") Long patientId);
}
