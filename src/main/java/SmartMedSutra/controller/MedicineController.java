package SmartMedSutra.controller;

import SmartMedSutra.dto.MedicineAlternativeResponse;
import SmartMedSutra.service.MedicineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medicines")
@RequiredArgsConstructor
@Tag(name = "Generic Medicine Recommendations", description = "Compare brand vs. generic medicine prices")
public class MedicineController {

    private final MedicineService medicineService;

    @GetMapping("/alternatives/{medicineName}")
    @Operation(
        summary = "Get generic alternative",
        description = "Returns brand price, generic alternative name, generic price, and total savings for a given medicine."
    )
    public ResponseEntity<MedicineAlternativeResponse> getAlternative(
            @PathVariable String medicineName) {
        return ResponseEntity.ok(medicineService.getAlternative(medicineName));
    }
}
