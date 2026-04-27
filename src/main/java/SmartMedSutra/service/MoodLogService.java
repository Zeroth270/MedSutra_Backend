package SmartMedSutra.service;

import SmartMedSutra.dto.MoodLogRequest;
import SmartMedSutra.dto.MoodLogResponse;
import SmartMedSutra.entity.MoodLog;
import SmartMedSutra.entity.User;
import SmartMedSutra.repository.MoodLogRepository;
import SmartMedSutra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MoodLogService {

    private final MoodLogRepository moodLogRepository;
    private final UserRepository userRepository;

    // ── Log Mood ───────────────────────────────────────────────

    public MoodLogResponse logMood(MoodLogRequest request) {
        User patient = userRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + request.getPatientId()));

        MoodLog moodLog = MoodLog.builder()
                .patient(patient)
                .beforeMood(request.getBeforeMood())
                .afterMood(request.getAfterMood())
                .symptomScore(request.getSymptomScore())
                .build();

        MoodLog saved = moodLogRepository.save(moodLog);
        return mapToResponse(saved);
    }

    // ── Get Mood Logs by Patient ID ────────────────────────────

    public List<MoodLogResponse> getMoodLogsByPatientId(Long patientId) {
        if (!userRepository.existsById(patientId)) {
            throw new RuntimeException("Patient not found with id: " + patientId);
        }

        return moodLogRepository.findByPatientIdOrderByTimestampDesc(patientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ── Helper ─────────────────────────────────────────────────

    private MoodLogResponse mapToResponse(MoodLog log) {
        return MoodLogResponse.builder()
                .moodId(log.getMoodId())
                .patientId(log.getPatient().getId())
                .patientName(log.getPatient().getName())
                .beforeMood(log.getBeforeMood())
                .afterMood(log.getAfterMood())
                .symptomScore(log.getSymptomScore())
                .timestamp(log.getTimestamp())
                .build();
    }
}
