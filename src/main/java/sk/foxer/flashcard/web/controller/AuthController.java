package sk.foxer.flashcard.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.foxer.flashcard.domain.service.AuthService;
import sk.foxer.flashcard.web.dto.auth.LoginRequestDto;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Value("${app.cookies.secure:false}")
    private boolean secureCookies;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequestDto dto) {
        HttpHeaders headers = new HttpHeaders();
        Map<String, Object> body = authService.login(dto, headers, secureCookies);
        return ResponseEntity.ok().headers(headers).body(body);
    }


    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        HttpHeaders headers = new HttpHeaders();
        Map<String, Object> resp = authService.refresh(refreshToken, headers, secureCookies);
        return ResponseEntity.ok().headers(headers).body(resp);
    }
}
