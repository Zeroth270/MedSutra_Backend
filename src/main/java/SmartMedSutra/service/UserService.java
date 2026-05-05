package SmartMedSutra.service;

import SmartMedSutra.dto.UserResponse;
import SmartMedSutra.dto.UserUpdateRequest;
import SmartMedSutra.entity.User;
import SmartMedSutra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // ── Get User by ID ─────────────────────────────────────────

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        return mapToResponse(user);
    }

    // ── Update User ────────────────────────────────────────────

    public UserResponse updateUser(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setAge(request.getAge());

        User updatedUser = userRepository.save(user);
        return mapToResponse(updatedUser);
    }

    // ── Delete User ────────────────────────────────────────────

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    // ── FCM Token ──────────────────────────────────────────────

    public void saveFcmToken(Long userId, String token) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        user.setFcmToken(token);
        userRepository.save(user);
    }

    // ── Helper ─────────────────────────────────────────────────

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .phone(user.getPhone())
                .age(user.getAge())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
