package sk.foxer.flashcard.web.mapper;

import sk.foxer.flashcard.domain.model.Flashcard;
import sk.foxer.flashcard.web.dto.flashcard.FlashcardDto;

public class FlashcardMapper {

    public static FlashcardDto toDto(Flashcard flashcard) {
        if (flashcard == null) return null;
        FlashcardDto dto = new FlashcardDto();
        dto.setId(flashcard.getId());
        dto.setFrontText(flashcard.getFrontText());
        dto.setBackText(flashcard.getBackText());
        return dto;
    }

    public static Flashcard toEntity(FlashcardDto dto) {
        if (dto == null) return null;
        Flashcard flashcard = new Flashcard();
        flashcard.setId(dto.getId());
        flashcard.setFrontText(dto.getFrontText());
        flashcard.setBackText(dto.getBackText());
        return flashcard;
    }
}
