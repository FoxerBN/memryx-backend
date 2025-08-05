package sk.foxer.flashcard.web.mapper.deckmapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import sk.foxer.flashcard.domain.model.Deck;
import sk.foxer.flashcard.web.dto.deck.DeckCreateRequestDto;
import sk.foxer.flashcard.web.dto.deck.DeckDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeckMapper {
    @Mapping(target = "folderId", source = "folder.id")
    DeckDto toDto(Deck deck);
    List<DeckDto> toDtoList(List<Deck> decks);
    Deck toEntity(DeckCreateRequestDto dto);

    @AfterMapping
    default void setDeckForFlashcards(@MappingTarget Deck deck) {
        if (deck.getFlashcards() != null) {
            deck.getFlashcards().forEach(f -> f.setDeck(deck));
        }
    }
}
