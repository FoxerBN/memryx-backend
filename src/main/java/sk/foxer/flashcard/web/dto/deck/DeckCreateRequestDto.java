package sk.foxer.flashcard.web.dto.deck;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import sk.foxer.flashcard.web.dto.flashcard.FlashcardCreateRequestDto;

import java.util.List;

@Getter
@Setter
public class DeckCreateRequestDto {
    @NotBlank(message = "Deck name must not be blank")
    private String name;

    private String description;

    @NotEmpty(message = "At least one flashcard is required")
    private List<FlashcardCreateRequestDto> flashcards;
}
