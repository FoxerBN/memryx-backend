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

/**
 * Service for handling authentication and token management.
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AppUserRepository userRepository;
    private final JwtService jwtService;

    /**
     * Logs in a user by generating access and refresh tokens.
     * Stores tokens in cookies for secure client storage.
     *
     * @param dto             login request data
     * @param responseHeaders headers to add cookies
     * @param secureCookies   whether cookies should be marked as secure (for HTTPS)
     * @return a map with basic user info
     */
    public Map<String, Object> login(LoginRequestDto dto,
                                     HttpHeaders responseHeaders,
                                     boolean secureCookies) {

        AppUser user = userRepository.findByUsername(dto.getUsername());
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + dto.getUsername());
        }

        // Short-lived access token (15 min)
        Duration accessExp = Duration.ofMinutes(15);
        String accessJwt = jwtService.generateToken(user, accessExp);

        ResponseCookie accessCookie = ResponseCookie
                .from("access_token", accessJwt)
                .httpOnly(true)
                .secure(secureCookies)
                .sameSite(secureCookies ? "None" : "Lax")
                .path("/")
                .maxAge(accessExp)
                .build();

        responseHeaders.add(HttpHeaders.SET_COOKIE, accessCookie.toString());

        // Optional: long-lived refresh token (6 months) if user requests "stay logged in"
        if (dto.isStayLoggedIn()) {
            Duration refreshExp = Duration.ofDays(180);
            String refreshJwt = jwtService.generateToken(user, refreshExp);

            ResponseCookie refreshCookie = ResponseCookie
                    .from("refresh_token", refreshJwt)
                    .httpOnly(true)
                    .secure(secureCookies)
                    .sameSite(secureCookies ? "None" : "Lax")
                    .path("/api/auth/refresh") // restricts CSRF surface
                    .maxAge(refreshExp)
                    .build();

            responseHeaders.add(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        }

        return Map.of(
                "username", user.getUsername(),
                "userId", user.getId()
        );
    }

    /**
     * Refreshes the access token using a valid refresh token.
     * Rotates the refresh token and issues a new access token.
     *
     * @param refreshToken    the existing refresh token from cookie
     * @param responseHeaders headers to add new cookies
     * @param secureCookies   whether cookies should be marked as secure (for HTTPS)
     * @return a map with a message and user info
     */
    public Map<String, Object> refresh(String refreshToken,
                                       HttpHeaders responseHeaders,
                                       boolean secureCookies) {

        if (jwtService.isValid(refreshToken)) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        String username = jwtService.extractUsername(refreshToken);
        AppUser user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found: " + username);
        }

        // Rotate refresh token (issue new one)
        Duration refreshExp = Duration.ofDays(180);
        String newRefresh = jwtService.generateToken(user, refreshExp);
        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", newRefresh)
                .httpOnly(true)
                .secure(secureCookies)
                .sameSite(secureCookies ? "None" : "Lax")
                .path("/api/auth/refresh")
                .maxAge(refreshExp)
                .build();
        responseHeaders.add(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        // Issue new short-lived access token
        Duration accessExp = Duration.ofMinutes(15);
        String accessJwt = jwtService.generateToken(user, accessExp);
        ResponseCookie accessCookie = ResponseCookie.from("access_token", accessJwt)
                .httpOnly(true)
                .secure(secureCookies)
                .sameSite(secureCookies ? "None" : "Lax")
                .path("/")
                .maxAge(accessExp)
                .build();
        responseHeaders.add(HttpHeaders.SET_COOKIE, accessCookie.toString());

        return Map.of(
                "message", "access token refreshed",
                "username", user.getUsername(),
                "userId", user.getId()
        );
    }

}