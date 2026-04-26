package SmartMedSutra.dto;

import SmartMedSutra.entity.RelationshipType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CareTeamRequest {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    private Long caregiverId;

    private Long doctorId;

    @NotNull(message = "Relationship type is required")
    private RelationshipType relationshipType;
}
