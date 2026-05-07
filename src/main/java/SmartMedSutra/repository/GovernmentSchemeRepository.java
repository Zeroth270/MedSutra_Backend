package SmartMedSutra.repository;

import SmartMedSutra.entity.GovernmentScheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GovernmentSchemeRepository extends JpaRepository<GovernmentScheme, Long> {

    List<GovernmentScheme> findByStateIgnoreCase(String state);
}
