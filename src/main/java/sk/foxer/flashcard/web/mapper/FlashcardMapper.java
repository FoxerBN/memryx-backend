package sk.foxer.flashcard.web.mapper;

import org.mapstruct.Mapper;
import sk.foxer.flashcard.domain.model.Flashcard;
import sk.foxer.flashcard.web.dto.flashcard.FlashcardDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FlashcardMapper {
    FlashcardDto toDto(Flashcard flashcard);
    Flashcard toEntity(FlashcardDto dto);
    List<FlashcardDto> toDtoList(List<Flashcard> flashcards);
    List<Flashcard> toEntityList(List<FlashcardDto> dtos);
}