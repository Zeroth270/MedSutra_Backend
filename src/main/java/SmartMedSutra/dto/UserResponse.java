package SmartMedSutra.dto;

import SmartMedSutra.entity.Role;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private Role role;
    private String phone;
    private Integer age;
    private LocalDateTime createdAt;
}
