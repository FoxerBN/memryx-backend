package sk.foxer.flashcard.web.controller;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.foxer.flashcard.domain.model.Deck;
import sk.foxer.flashcard.domain.service.DeckService;
import sk.foxer.flashcard.web.dto.deck.DeckCreateRequestDto;
import sk.foxer.flashcard.web.dto.deck.DeckDto;
import sk.foxer.flashcard.web.dto.deck.DeckSummaryDto;
import sk.foxer.flashcard.web.mapper.deckmapper.DeckMapper;
import sk.foxer.flashcard.web.mapper.deckmapper.DeckSummaryMapper;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/user/all/{userId}")
    public List<DeckSummaryDto> getAllDecksByUser(@PathVariable Long userId) {
        List<Deck> decks = deckService.getAllDeckByUser(userId);
        return deckSummaryMapper.toSummaryDtoList(decks);
    }

    @PostMapping("/create/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public DeckDto createDeck(
            @PathVariable Long userId,
            @Valid @RequestBody DeckCreateRequestDto dto
    ) {
        return deckService.createDeck(userId, dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteDeck(@PathVariable Long id) {
        deckService.deleteDeck(id);
        return ResponseEntity.ok(Map.of("message", "Deck with id " + id + " deleted successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateDeck(
            @PathVariable Long id,
            @Valid @RequestBody DeckCreateRequestDto dto
    ) {
        DeckDto updatedDeck = deckService.updateDeck(id, dto);
        return ResponseEntity.ok(Map.of("message", "Deck updated successfully", "deck", updatedDeck));
    }
}
