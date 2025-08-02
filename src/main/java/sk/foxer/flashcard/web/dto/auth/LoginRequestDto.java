package sk.foxer.flashcard.web.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginRequestDto {
    private String username;
    private boolean stayLoggedIn;
}
