package SmartMedSutra.service;

import SmartMedSutra.entity.Reminder;
import SmartMedSutra.repository.ReminderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReminderSchedulerService {

    private final ReminderRepository reminderRepository;
    private final NotificationService notificationService;

    // Run at the top of every minute (e.g., 09:00:00, 09:01:00)
    @Scheduled(cron = "0 * * * * *")
    @Transactional(readOnly = true)
    public void processScheduledReminders() {
        LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
        log.info("Checking for scheduled reminders at: {}", now);

        List<Reminder> dueReminders = reminderRepository.findByReminderTimeAndActiveTrue(now);

        for (Reminder reminder : dueReminders) {
            String fcmToken = reminder.getPatient().getFcmToken();
            if (fcmToken != null && !fcmToken.isEmpty()) {
                try {
                    String title = "Medication Reminder";
                    String body = reminder.getMessage();
                    
                    // If medication is attached, add it to the title
                    if (reminder.getMedication() != null) {
                        title = "Time for " + reminder.getMedication().getName();
                    }

                    notificationService.sendNotification(fcmToken, title, body);
                    log.info("Successfully sent reminder notification to user ID: {} for reminder ID: {}", 
                            reminder.getPatient().getId(), reminder.getId());
                } catch (Exception e) {
                    log.error("Failed to send notification to user ID: " + reminder.getPatient().getId(), e);
                }
            } else {
                log.warn("User ID {} has no FCM token. Cannot send scheduled reminder ID: {}", 
                        reminder.getPatient().getId(), reminder.getId());
            }
        }
    }
}
