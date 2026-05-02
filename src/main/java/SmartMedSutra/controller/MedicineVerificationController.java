package SmartMedSutra.controller;

import SmartMedSutra.entity.VerificationLog;
import SmartMedSutra.service.MedicineVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/verification")
@RequiredArgsConstructor
@CrossOrigin("*")
public class MedicineVerificationController {

    private final MedicineVerificationService medicineVerificationService;

    @PostMapping("/verify")
    public ResponseEntity<?> verifyMedicine(
            @RequestParam("patientId") Long patientId,
            @RequestParam("medicationId") Long medicationId,
            @RequestParam("image") MultipartFile image) {

        try {
            VerificationLog log = medicineVerificationService.verifyMedicine(patientId, medicationId, image);
            return ResponseEntity.ok(log);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
