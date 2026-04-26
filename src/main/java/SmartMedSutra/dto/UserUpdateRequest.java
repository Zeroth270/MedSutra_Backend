package SmartMedSutra.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private String phone;

    @Min(value = 0, message = "Age must be positive")
    @Max(value = 150, message = "Age must be realistic")
    private Integer age;
}
