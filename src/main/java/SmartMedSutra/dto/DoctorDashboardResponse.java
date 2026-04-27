package SmartMedSutra.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorDashboardResponse {

    private Long doctorId;
    private String doctorName;
    private List<DashboardResponse> patientDashboards;
}
