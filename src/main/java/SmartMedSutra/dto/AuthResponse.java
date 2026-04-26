package SmartMedSutra.dto;

import SmartMedSutra.entity.Role;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private Long id;
    private String name;
    private String email;
    private Role role;
    private String token;
    private String message;
}
