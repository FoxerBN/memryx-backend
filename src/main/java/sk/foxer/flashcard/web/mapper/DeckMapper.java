package sk.foxer.flashcard.web.mapper;

import sk.foxer.flashcard.domain.model.Deck;
import sk.foxer.flashcard.web.dto.deck.DeckDto;

import java.util.stream.Collectors;

public class DeckMapper {

    public static DeckDto toDto(Deck deck) {
        if (deck == null) return null;
        DeckDto dto = new DeckDto();
        dto.setId(deck.getId());
        dto.setName(deck.getName());
        dto.setDescription(deck.getDescription());

        // Ak chceš zahrnúť aj flashcards
        if (deck.getFlashcards() != null) {
            dto.setFlashcards(deck.getFlashcards()
                    .stream()
                    .map(FlashcardMapper::toDto)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    public static Deck toEntity(DeckDto dto) {
        if (dto == null) return null;
        Deck deck = new Deck();
        deck.setId(dto.getId());
        deck.setName(dto.getName());
        deck.setDescription(dto.getDescription());
        // flashcards na entitu väčšinou mapuješ, keď vytváraš/vkladáš deck
        return deck;
    }
}
