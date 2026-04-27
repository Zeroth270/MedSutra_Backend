package SmartMedSutra.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaregiverDashboardResponse {

    private Long caregiverId;
    private String caregiverName;
    private List<DashboardResponse> patientDashboards;
}
