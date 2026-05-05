package SmartMedSutra.controller;

import SmartMedSutra.dto.UserResponse;
import SmartMedSutra.dto.UserUpdateRequest;
import SmartMedSutra.entity.User;
import SmartMedSutra.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Users", description = "User profile management APIs")
public class UserController {

    private final UserService userService;

    // GET /users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    // PUT /users/update
    @PutMapping("/update")
    public ResponseEntity<UserResponse> updateUser(@AuthenticationPrincipal User currentUser,
                                                   @Valid @RequestBody UserUpdateRequest request) {
        UserResponse response = userService.updateUser(currentUser.getId(), request);
        return ResponseEntity.ok(response);
    }

    // DELETE /users/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    // POST /users/save-token
    @PostMapping("/save-token")
    public ResponseEntity<String> saveToken(@AuthenticationPrincipal User currentUser,
                                            @RequestBody Map<String, String> body) {
        String token = body.get("token");
        if (token != null && !token.isEmpty()) {
            userService.saveFcmToken(currentUser.getId(), token);
            return ResponseEntity.ok("Token saved successfully");
        }
        return ResponseEntity.badRequest().body("Token is required");
    }
}
