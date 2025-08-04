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


    /**
     * Logs in a user by generating access and refresh tokens.
     * Sets cookies in the response headers for secure storage.
     *
     * @param dto             the login request data transfer object
     * @param responseHeaders headers to add cookies
     * @param secureCookies   whether to set cookies as secure
     * @return a map containing the username and userId
     */
    public Map<String, Object> login(LoginRequestDto dto,
                                     HttpHeaders responseHeaders,
                                     boolean secureCookies) {

        AppUser user = userRepository.findByUsername(dto.getUsername());
        if (user == null) {
            throw new ResourceNotFoundException(
                    "User not found with username: " + dto.getUsername());
        }

        Duration accessExp  = Duration.ofMinutes(15);
        String   accessJwt  = jwtService.generateToken(user, accessExp);

        ResponseCookie accessCookie = ResponseCookie
                .from("access_token", accessJwt)
                .httpOnly(true)
                .secure(secureCookies)          // true in prod
                .sameSite(secureCookies ? "None" : "Lax")
                .path("/")                      // sent to every endpoint
                .maxAge(accessExp)
                .build();

        responseHeaders.add(HttpHeaders.SET_COOKIE, accessCookie.toString());

        /* ---------- OPTIONAL: long-term “remember me” ---------- */
        if (dto.isStayLoggedIn()) {
            Duration refreshExp = Duration.ofDays(180);        // 6 months
            String   refreshJwt = jwtService.generateToken(user, refreshExp);

            ResponseCookie refreshCookie = ResponseCookie
                    .from("refresh_token", refreshJwt)
                    .httpOnly(true)
                    .secure(secureCookies)
                    .sameSite(secureCookies ? "None" : "Lax")
                    .path("/api/auth/refresh")   // <-- limits CSRF surface
                    .maxAge(refreshExp)
                    .build();

            responseHeaders.add(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        }
        /* ------------------------------------------------------- */

        return Map.of(
                "username", user.getUsername(),
                "userId",   user.getId()
        );
    }

    /**
     * Refreshes the access token using a valid refresh token.
     * Rotates the refresh token and issues a new access token.
     *
     * @param refreshToken    the existing refresh token
     * @param responseHeaders headers to add new cookies
     * @param secureCookies   whether to set cookies as secure
     * @return a map containing the message, username, and userId
     */
    public Map<String, Object> refresh(String refreshToken,
                                       HttpHeaders responseHeaders,
                                       boolean secureCookies) {

        if (!jwtService.isValid(refreshToken)) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        String username = jwtService.extractUsername(refreshToken);
        AppUser user    = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found: " + username);
        }

        /* 1️⃣ rotate refresh-token (recommended) */
        Duration refreshExp = Duration.ofDays(180);
        String   newRefresh = jwtService.generateToken(user, refreshExp);
        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", newRefresh)
                .httpOnly(true)
                .secure(secureCookies)
                .sameSite(secureCookies ? "None" : "Lax")
                .path("/api/auth/refresh")
                .maxAge(refreshExp)
                .build();
        responseHeaders.add(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        /* 2️⃣ mint short-lived access-token */
        Duration accessExp = Duration.ofMinutes(15);
        String   accessJwt = jwtService.generateToken(user, accessExp);
        ResponseCookie accessCookie = ResponseCookie.from("access_token", accessJwt)
                .httpOnly(true)
                .secure(secureCookies)
                .sameSite(secureCookies ? "None" : "Lax")
                .path("/")
                .maxAge(accessExp)
                .build();
        responseHeaders.add(HttpHeaders.SET_COOKIE, accessCookie.toString());

        return Map.of(
                "message",  "access token refreshed",
                "username", user.getUsername(),
                "userId",   user.getId()
        );
    }

}
