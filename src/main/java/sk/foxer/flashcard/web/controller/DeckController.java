package sk.foxer.flashcard.web.controller;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sk.foxer.flashcard.domain.model.Deck;
import sk.foxer.flashcard.domain.service.DeckService;
import sk.foxer.flashcard.web.dto.deck.DeckCreateRequestDto;
import sk.foxer.flashcard.web.dto.deck.DeckDto;
import sk.foxer.flashcard.web.dto.deck.DeckSummaryDto;
import sk.foxer.flashcard.web.mapper.deckmapper.DeckMapper;
import sk.foxer.flashcard.web.mapper.deckmapper.DeckSummaryMapper;

import java.util.List;

@RestController
@RequestMapping("/api/deck")
@AllArgsConstructor
public class DeckController {
    private final DeckService deckService;
    private final DeckSummaryMapper deckSummaryMapper;
    private final DeckMapper deckMapper;

    @GetMapping("/all")
    public List<DeckSummaryDto> getAllDecks() {
        List<Deck> decks = deckService.getAllDecks();
        return deckSummaryMapper.toSummaryDtoList(decks);
    }

    @GetMapping("/{id}")
    public DeckDto getDeckById(@PathVariable Long id) {
        Deck deck = deckService.getDeckById(id);
        return deckMapper.toDto(deck);
    }

    @PostMapping("/create/{userId}")
    public DeckDto createDeck(
            @PathVariable Long userId,
            @Valid @RequestBody DeckCreateRequestDto dto
    ) {
        return deckService.createDeck(userId, dto);
    }
}
