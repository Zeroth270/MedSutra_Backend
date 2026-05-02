package SmartMedSutra.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medicine_reference")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineReference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String color;

    private String shape;

    private String size;

    @Column(name = "imprint_text")
    private String imprintText;

}
