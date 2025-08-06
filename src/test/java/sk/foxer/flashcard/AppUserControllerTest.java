package sk.foxer.flashcard;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sk.foxer.flashcard.domain.model.AppUser;
import sk.foxer.flashcard.domain.repository.AppUserRepository;
import sk.foxer.flashcard.domain.service.JwtService;
import sk.foxer.flashcard.web.dto.appuser.AppUserCreateRequestDto;
import sk.foxer.flashcard.web.dto.appuser.AppUserUpdateRequestDto;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AppUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AppUserRepository appUserRepository;

    private Long createUser(String username, String displayName) throws Exception {
        AppUserCreateRequestDto dto = new AppUserCreateRequestDto();
        dto.setUsername(username);
        dto.setDisplayName(displayName);

        String response = mockMvc.perform(post("/api/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(response).get("id").asLong();
    }

    private Cookie getAuthCookie(String username) throws Exception {
        AppUser user = appUserRepository.findByUsername(username);
        assertNotNull(user, "User must exist before generating JWT");
        String token = jwtService.generateToken(user, Duration.ofMinutes(15));
        return new Cookie("access_token", token);
    }

    @Test
    void shouldCreateUser() throws Exception {
        Long id = createUser("user1", "UserDisplay");

        mockMvc.perform(get("/api/user/" + id)
                        .cookie(getAuthCookie("user1")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"));
    }

    @Test
    void shouldReturnUserById() throws Exception {
        Long userId = createUser("fetch_user", "Fetch");

        mockMvc.perform(get("/api/user/" + userId)
                        .cookie(getAuthCookie("fetch_user")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("fetch_user"));
    }

    @Test
    void shouldUpdateUserDisplayName() throws Exception {
        Long id = createUser("update_user", "BeforeUpdate");

        AppUserUpdateRequestDto updateDto = new AppUserUpdateRequestDto();
        updateDto.setDisplayName("UpdatedName");

        mockMvc.perform(put("/api/user/" + id)
                        .cookie(getAuthCookie("update_user"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.displayName").value("UpdatedName"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        Long id = createUser("delete_me", "DeleteUser");

        mockMvc.perform(delete("/api/user/" + id)
                        .cookie(getAuthCookie("delete_me")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(id.intValue()));
    }

    @Test
    void shouldReturnNotFoundForMissingUser() throws Exception {
        Long id = createUser("notfound_user", "NF");

        mockMvc.perform(get("/api/user/99999")
                        .cookie(getAuthCookie("notfound_user")))
                .andExpect(status().isNotFound());
    }
}