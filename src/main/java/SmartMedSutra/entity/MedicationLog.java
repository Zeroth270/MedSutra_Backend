package SmartMedSutra.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "medication_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long medLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medication_id", nullable = false)
    private Medication medication;

    @Column(nullable = false)
    private boolean taken;

    @Column(name = "taken_time")
    private LocalDateTime takenTime;

    @Column(name = "delay_minutes")
    private Integer delayMinutes;
}
