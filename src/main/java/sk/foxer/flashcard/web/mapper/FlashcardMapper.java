package sk.foxer.flashcard.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sk.foxer.flashcard.domain.model.Flashcard;
import sk.foxer.flashcard.web.dto.flashcard.FlashcardCreateRequestDto;

@Mapper(componentModel = "spring")
public interface FlashcardMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deck", ignore = true)
    Flashcard toEntity(FlashcardCreateRequestDto dto);
}
