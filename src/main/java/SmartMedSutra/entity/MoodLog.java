package SmartMedSutra.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mood_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoodLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long moodId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @Min(1) @Max(5)
    @Column(name = "before_mood")
    private int beforeMood;

    @Min(1) @Max(5)
    @Column(name = "after_mood")
    private int afterMood;

    @Min(1) @Max(10)
    @Column(name = "symptom_score")
    private int symptomScore;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
}
