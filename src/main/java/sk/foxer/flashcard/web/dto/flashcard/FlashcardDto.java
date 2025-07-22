package sk.foxer.flashcard.web.dto.flashcard;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlashcardDto {
    private Long id;
    private String frontText;
    private String backText;
}
