package sk.foxer.flashcard.web.mapper.deckmapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sk.foxer.flashcard.domain.model.Deck;
import sk.foxer.flashcard.web.dto.deck.DeckCreateRequestDto;
import sk.foxer.flashcard.web.dto.deck.DeckDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeckMapper {

    @Mapping(target = "folderId", source = "folder.id")
    DeckDto toDto(Deck deck);
    List<DeckDto> toDtoList(List<Deck> decks);

    // Entity sa vytvorí bez vzťahov; tie nastaví service
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "folder", ignore = true)
    @Mapping(target = "flashcards", ignore = true)
    Deck toEntity(DeckCreateRequestDto dto);
}
