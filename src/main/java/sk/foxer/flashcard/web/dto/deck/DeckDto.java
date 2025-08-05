package sk.foxer.flashcard.web.dto.deck;

import lombok.Getter;
import lombok.Setter;
import sk.foxer.flashcard.web.dto.flashcard.FlashcardDto;

import java.util.List;

@Getter
@Setter
public class DeckDto {
    private Long id;
    private String name;
    private String description;
    private Long folderId;
    private List<FlashcardDto> flashcards;

}
