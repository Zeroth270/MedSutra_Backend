package SmartMedSutra.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "environment_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnvironmentData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long environmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    private Double aqi;

    private Double temperature;

    private Double humidity;

    private String location;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
}
