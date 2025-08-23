package sk.foxer.flashcard.web.dto.deck;

import lombok.Getter;

@Getter
public class DeckSummaryDto {
    private final Long id;
    private final String name;
    private final String description;
    private final long flashcardCount;


    public DeckSummaryDto(Long id, String name, String description, long flashcardCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.flashcardCount = flashcardCount;
    }
}