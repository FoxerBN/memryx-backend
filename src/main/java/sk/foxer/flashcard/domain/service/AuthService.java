package sk.foxer.flashcard.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import sk.foxer.flashcard.api.exception.ResourceNotFoundException;
import sk.foxer.flashcard.domain.model.AppUser;
import sk.foxer.flashcard.domain.repository.AppUserRepository;
import sk.foxer.flashcard.web.dto.auth.LoginRequestDto;

import java.time.Duration;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AppUserRepository userRepository;
    private final JwtService jwtService;


    public Map<String, Object> login(LoginRequestDto dto, HttpHeaders responseHeaders, boolean secureCookies) {
        AppUser user = userRepository.findByUsername(dto.getUsername());
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + dto.getUsername());
        }

        Duration accessExp = Duration.ofMinutes(15);
        Duration refreshExp = dto.isStayLoggedIn() ? Duration.ofDays(180) : Duration.ofHours(1);

        String accessToken  = jwtService.generateToken(user, accessExp);
        String refreshToken = jwtService.generateToken(user, refreshExp);

        ResponseCookie accessCookie = ResponseCookie.from("access_token", accessToken)
                .httpOnly(true)
                .secure(secureCookies)
                .sameSite(secureCookies ? "None" : "Lax")
                .path("/")
                .maxAge(accessExp)
                .build();

        responseHeaders.add(HttpHeaders.SET_COOKIE, accessCookie.toString());

        return Map.of(
                "refreshToken", refreshToken,
                "username", user.getUsername(),
                "userId", user.getId()
        );
    }

    public Map<String, Object> refresh(String refreshToken,
                                       HttpHeaders responseHeaders,
                                       boolean secureCookies) {
        if (refreshToken == null || !jwtService.isValid(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String username = jwtService.extractUsername(refreshToken);
        AppUser user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }

        // vydáme nový krátky access token a dáme ho do HttpOnly cookie
        Duration accessExp = Duration.ofMinutes(15);
        String newAccessToken = jwtService.generateToken(user, accessExp);

        ResponseCookie accessCookie = ResponseCookie.from("access_token", newAccessToken)
                .httpOnly(true)
                .secure(secureCookies)
                .sameSite(secureCookies ? "None" : "Lax")
                .path("/")
                .maxAge(accessExp)
                .build();

        responseHeaders.add(HttpHeaders.SET_COOKIE, accessCookie.toString());

        // refresh token NErotujeme (zostáva ten istý až do expirácie)
        return Map.of(
                "message", "access token refreshed",
                "username", user.getUsername(),
                "userId", user.getId()
        );
    }
}
