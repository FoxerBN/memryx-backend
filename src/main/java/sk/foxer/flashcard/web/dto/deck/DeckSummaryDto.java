package sk.foxer.flashcard.web.dto.deck;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeckSummaryDto {
    private Long id;
    private String name;
    private String description;
    private int flashcardCount;
}