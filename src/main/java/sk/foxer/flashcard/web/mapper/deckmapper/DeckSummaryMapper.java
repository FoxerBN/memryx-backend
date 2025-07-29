package sk.foxer.flashcard.web.mapper.deckmapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sk.foxer.flashcard.domain.model.Deck;
import sk.foxer.flashcard.web.dto.deck.DeckSummaryDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeckSummaryMapper {
    @Mapping(target = "flashcardCount", expression = "java(deck.getFlashcards() != null ? deck.getFlashcards().size() : 0)")
    DeckSummaryDto toSummaryDto(Deck deck);

    List<DeckSummaryDto> toSummaryDtoList(List<Deck> decks);
}