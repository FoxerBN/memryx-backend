package sk.foxer.flashcard.web.mapper;

import org.mapstruct.Mapper;
import sk.foxer.flashcard.domain.model.Deck;
import sk.foxer.flashcard.web.dto.deck.DeckDto;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DeckMapper {
    DeckDto toDto(Deck deck);
    List<DeckDto> toDtoList(List<Deck> decks);
}
