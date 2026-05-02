package SmartMedSutra.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medication_id")
    private Medication medication;

    @Column(name = "detected_name")
    private String detectedName;

    @Column(name = "confidence_score")
    private Double confidenceScore;

    // e.g. MATCH, MISMATCH
    private String result;

    private LocalDateTime timestamp;
}
