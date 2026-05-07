package SmartMedSutra.repository;

import SmartMedSutra.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {

    List<Hospital> findByCityIgnoreCase(String city);

    List<Hospital> findByStateIgnoreCase(String state);

    List<Hospital> findByAyushmanSupported(boolean ayushmanSupported);

    List<Hospital> findByCityIgnoreCaseAndAyushmanSupported(String city, boolean ayushmanSupported);
}
