package SmartMedSutra.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medicine_prices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicinePrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "medicine_name", nullable = false)
    private String medicineName;

    @Column(name = "brand_price")
    private Double brandPrice;

    @Column(name = "generic_name")
    private String genericName;

    @Column(name = "generic_price")
    private Double genericPrice;

    private String manufacturer;
}
