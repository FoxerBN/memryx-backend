package sk.foxer.flashcard.web.dto.deck;

import lombok.Getter;

@Getter
public class DeckSummaryDto {
    private Long id;
    private String name;
    private String description;
    private long flashcardCount;


    public DeckSummaryDto(Long id, String name, String description, long flashcardCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.flashcardCount = flashcardCount;
    }
}