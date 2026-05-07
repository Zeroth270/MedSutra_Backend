package SmartMedSutra.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hospitals")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String address;

    private String city;

    private String state;

    private Double latitude;

    private Double longitude;

    private String phone;

    @Column(name = "ayushman_supported")
    private boolean ayushmanSupported;
}
