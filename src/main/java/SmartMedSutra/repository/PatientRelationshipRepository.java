package SmartMedSutra.repository;

import SmartMedSutra.entity.PatientRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRelationshipRepository extends JpaRepository<PatientRelationship, Long> {

    List<PatientRelationship> findByPatientId(Long patientId);
}
