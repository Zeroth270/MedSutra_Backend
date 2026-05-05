package SmartMedSutra.controller;

import SmartMedSutra.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notify")
public class NotificationController {

    @Autowired
    private NotificationService service;

    @PostMapping
    public String notifyUser(@RequestParam String token) throws Exception {
        return service.sendNotification(token, "Medicine Reminder", "Take your medicine now");
    }
}
