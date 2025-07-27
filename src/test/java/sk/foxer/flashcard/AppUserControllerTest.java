package sk.foxer.flashcard;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sk.foxer.flashcard.web.dto.appuser.AppUserCreateRequestDto;
import sk.foxer.flashcard.web.dto.appuser.AppUserUpdateRequestDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AppUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

    @Test
    void shouldCreateUser() throws Exception {
        Long id = createUser("user1", "UserDisplay");

        mockMvc.perform(get("/api/user/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"));
    }

    @Test
    void shouldReturnUserById() throws Exception {
        Long userId = createUser("fetch_user", "Fetch");

        mockMvc.perform(get("/api/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("fetch_user"));
    }

    @Test
    void shouldUpdateUserDisplayName() throws Exception {
        Long id = createUser("update_user", "BeforeUpdate");

        AppUserUpdateRequestDto updateDto = new AppUserUpdateRequestDto();
        updateDto.setDisplayName("UpdatedName");

        mockMvc.perform(put("/api/user/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.displayName").value("UpdatedName"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        Long id = createUser("delete_me", "DeleteUser");

        mockMvc.perform(delete("/api/user/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(id.intValue()));
    }

    @Test
    void shouldReturnNotFoundForMissingUser() throws Exception {
        mockMvc.perform(get("/api/user/99999"))
                .andExpect(status().isNotFound());
    }
}
