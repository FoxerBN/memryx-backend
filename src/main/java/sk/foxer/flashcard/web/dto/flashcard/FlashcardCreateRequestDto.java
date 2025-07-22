package sk.foxer.flashcard.web.dto.flashcard;

public class FlashcardCreateRequestDto {
    private String frontText;
    private String backText;
    private String hint;
    // ... môžeš pridať deckId, ak budeš viazať flashcard na deck
}
