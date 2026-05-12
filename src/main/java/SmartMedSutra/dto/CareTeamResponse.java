package SmartMedSutra.dto;

import SmartMedSutra.entity.RelationshipType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CareTeamResponse {

    private Long id;
    private Long patientId;
    private String patientName;
    private Long caregiverId;
    private String caregiverName;
    private Long doctorId;
    private String doctorName;
    private String caregiverEmail;
    private String doctorEmail;
    private RelationshipType relationshipType;
}
