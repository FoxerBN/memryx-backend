package sk.foxer.flashcard.web.dto.appuser;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppUserCreateRequestDto {

    @NotBlank(message = "Username must not be blank")
    private String username;

    @NotBlank(message = "Display name must not be blank")
    private String displayName;
}