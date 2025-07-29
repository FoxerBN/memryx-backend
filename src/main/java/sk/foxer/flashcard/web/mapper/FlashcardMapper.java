package sk.foxer.flashcard.web.mapper;

import org.mapstruct.Mapper;
import sk.foxer.flashcard.domain.model.Flashcard;
import sk.foxer.flashcard.web.dto.flashcard.FlashcardCreateRequestDto;
import sk.foxer.flashcard.web.dto.flashcard.FlashcardDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FlashcardMapper {
    Flashcard toEntity(FlashcardCreateRequestDto dto);
    List<Flashcard> toEntityList(List<FlashcardCreateRequestDto> dtos);
}