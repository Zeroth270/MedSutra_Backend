package SmartMedSutra.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resultId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @Column(name = "adherence_score")
    private Double adherenceScore;

    @Column(name = "risk_level")
    private String riskLevel;

    @Column(columnDefinition = "TEXT")
    private String prediction;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
