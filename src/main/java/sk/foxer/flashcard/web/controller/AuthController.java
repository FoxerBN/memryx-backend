package sk.foxer.flashcard.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Map<String, Object>> login(HttpServletRequest req,
                                                     @RequestBody LoginRequestDto dto) {
        boolean secure = req.isSecure() ||
                "https".equalsIgnoreCase(req.getHeader("X-Forwarded-Proto"));
        HttpHeaders headers = new HttpHeaders();
        Map<String, Object> body = authService.login(dto, headers, secure);
        return ResponseEntity.ok().headers(headers).body(body);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String,Object>> logout(HttpServletRequest req) {
        boolean secureCookies =
                req.isSecure() ||
                        "https".equalsIgnoreCase(req.getHeader("X-Forwarded-Proto"));
        HttpHeaders headers = new HttpHeaders();
        Map<String,Object> body = authService.logout(headers, secureCookies);
        return ResponseEntity.ok().headers(headers).body(body);
    }


    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest req,
                                     @CookieValue(name = "refresh_token", required = false) String refreshToken) {

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Missing refresh token"));
        }

        boolean secure = req.isSecure() ||
                "https".equalsIgnoreCase(req.getHeader("X-Forwarded-Proto"));

        HttpHeaders headers = new HttpHeaders();
        Map<String, Object> body = authService.refresh(refreshToken, headers, secure);

        return ResponseEntity.ok().headers(headers).body(body);
    }
}
