package sk.foxer.flashcard.web.dto.appuser;

import lombok.Getter;
import lombok.Setter;
import sk.foxer.flashcard.web.dto.deck.DeckDto;

import java.util.List;

@Getter
@Setter
public class AppUserBasicDto {
    private Long id;
    private String username;
    private String displayName;
}
