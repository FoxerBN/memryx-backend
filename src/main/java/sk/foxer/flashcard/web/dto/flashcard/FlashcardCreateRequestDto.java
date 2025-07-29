package sk.foxer.flashcard.web.dto.flashcard;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlashcardCreateRequestDto {
    @NotBlank(message = "Front text must not be blank")
    private String frontText;

    @NotBlank(message = "Back text must not be blank")
    private String backText;
}