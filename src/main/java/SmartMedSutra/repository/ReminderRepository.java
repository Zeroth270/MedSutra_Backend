package SmartMedSutra.repository;

import SmartMedSutra.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    List<Reminder> findByPatientIdOrderByReminderTimeAsc(Long patientId);

    List<Reminder> findByReminderTimeAndActiveTrue(LocalTime reminderTime);
}
