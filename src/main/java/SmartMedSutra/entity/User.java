package SmartMedSutra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String phone;

    @Min(value = 0, message = "Age must be a positive number")
    @Max(value = 150, message = "Age must be realistic")
    private Integer age;

    @Column(name = "fcm_token")
    private String fcmToken;

    // ── Ayushman Eligibility Fields ────────────────────────────
    private String state;

    @Column(name = "annual_income")
    private Long annualIncome;

    @Column(name = "ration_card_type")
    private String rationCardType;

    @Column(name = "family_size")
    private Integer familySize;

    private String occupation;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ── Relationship ───────────────────────────────────────────

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Doctor doctor;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}

