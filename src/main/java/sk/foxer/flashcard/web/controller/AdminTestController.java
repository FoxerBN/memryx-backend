package sk.foxer.flashcard.web.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/test")
public class AdminTestController {

    @GetMapping("/auth")
    public Map<String, Object> testAuth(Authentication auth) {
        Map<String, Object> response = new HashMap<>();

        if (auth == null) {
            response.put("authenticated", false);
            response.put("message", "No authentication found");
        } else {
            response.put("authenticated", true);
            response.put("principal", auth.getName());
            response.put("authorities", auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()));
        }

        return response;
    }

    @GetMapping("/cookies")
    public Map<String, String> testCookies(HttpServletRequest request) {
        Map<String, String> cookies = new HashMap<>();

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                cookies.put(cookie.getName(), "PRESENT (value hidden)");
            }
        }

        return cookies;
    }
}