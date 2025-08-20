package sk.foxer.flashcard.web.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.foxer.flashcard.domain.service.DeckService;
import sk.foxer.flashcard.web.dto.deck.DeckCreateRequestDto;
import sk.foxer.flashcard.web.dto.deck.DeckDto;
import sk.foxer.flashcard.web.dto.deck.DeckSummaryDto;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/decks")
public class DeckController {

    private final DeckService deckService;

    public DeckController(DeckService deckService) {
        this.deckService = deckService;
    }

    @GetMapping
    public List<DeckDto> getAllDecks() {
        return deckService.getAllDecks();
    }

    @GetMapping("/summary")
    public List<DeckSummaryDto> getAllDeckSummaries() {
        return deckService.getAllDeckSummaries();
    }

    @GetMapping("/folder/{folderId}/summary")
    public List<DeckSummaryDto> getDeckSummariesByFolder(@PathVariable Long folderId) {
        return deckService.getDeckSummariesByFolder(folderId);
    }

    @GetMapping("/{id}")
    public DeckDto getDeckById(@PathVariable Long id) {
        return deckService.getDeckById(id);
    }

    @GetMapping("/folder/{folderId}")
    public List<DeckDto> getDecksByFolder(@PathVariable Long folderId) {
        return deckService.getDecksByFolderId(folderId);
    }

    @PostMapping
    public ResponseEntity<DeckDto> createDeck(@Valid @RequestBody DeckCreateRequestDto deckDto) {
        DeckDto created = deckService.createDeck(deckDto);
        return ResponseEntity.created(URI.create("/api/decks/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeckDto> updateDeck(@PathVariable Long id,
                                              @Valid @RequestBody DeckCreateRequestDto deckDto) {
        return ResponseEntity.ok(deckService.updateDeck(id, deckDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteDeck(@PathVariable Long id) {
        deckService.deleteDeck(id);
        Map<String, String> resp = new HashMap<>();
        resp.put("message", "Deck with id " + id + " deleted.");
        return ResponseEntity.ok(resp);
    }
}
