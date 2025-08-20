package sk.foxer.flashcard.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.foxer.flashcard.domain.service.AppUserService;
import sk.foxer.flashcard.web.dto.appuser.AppUserBasicDto;
import sk.foxer.flashcard.web.dto.appuser.AppUserCreateRequestDto;
import sk.foxer.flashcard.web.dto.appuser.AppUserUpdateRequestDto;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;

    @GetMapping
    public List<AppUserBasicDto> getAllUsers() {
        return appUserService.getAllUsers();
    }

    @GetMapping("/{id}")
    public AppUserBasicDto getUserById(@PathVariable Long id) {
        return appUserService.getUserById(id);
    }

    @PostMapping("/create")
    public AppUserBasicDto createUser(@Valid @RequestBody AppUserCreateRequestDto userDto) {
        return appUserService.createUser(userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        appUserService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "User with id " + id + " deleted successfully",
                "userId", id));
    }

    @PutMapping("/{id}")
    public AppUserBasicDto updateUser(@PathVariable Long id,
                                      @Valid @RequestBody AppUserUpdateRequestDto userDto) {
        return appUserService.updateDisplayName(id, userDto.getDisplayName());
    }
}
