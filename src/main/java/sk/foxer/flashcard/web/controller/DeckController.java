package sk.foxer.flashcard.web.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.foxer.flashcard.domain.model.Deck;
import sk.foxer.flashcard.domain.service.DeckService;
import sk.foxer.flashcard.web.dto.deck.DeckCreateRequestDto;
import sk.foxer.flashcard.web.dto.deck.DeckDto;
import sk.foxer.flashcard.web.dto.deck.DeckSummaryDto;
import sk.foxer.flashcard.web.mapper.deckmapper.DeckMapper;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/decks")
public class DeckController {

    private final DeckService deckService;
    private final DeckMapper deckMapper;

    public DeckController(DeckService deckService, DeckMapper deckMapper) {
        this.deckService = deckService;
        this.deckMapper = deckMapper;
    }

    /**
     * List all decks.
     * GET /api/decks
     */
    @GetMapping
    public List<DeckDto> getAllDecks() {
        return deckMapper.toDtoList(deckService.getAllDecks());
    }

    /**
     * List all deck summaries (name, description, flashcard count).
     * GET /api/decks/summary
     */
    @GetMapping("/summary")
    public List<DeckSummaryDto> getAllDeckSummaries() {
        return deckService.getAllDeckSummaries();
    }

    /**
     * List all deck summaries by folder.
     * GET /api/decks/folder/{folderId}/summary
     */
    @GetMapping("/folder/{folderId}/summary")
    public List<DeckSummaryDto> getDeckSummariesByFolder(@PathVariable Long folderId) {
        return deckService.getDeckSummariesByFolder(folderId);
    }

    /**
     * Get deck by id.
     * GET /api/decks/{id}
     */
    @GetMapping("/{id}")
    public DeckDto getDeckById(@PathVariable Long id) {
        return deckMapper.toDto(deckService.getDeckById(id));
    }

    @GetMapping("/folder/{folderId}")
    public List<DeckDto> getDecksByFolder(@PathVariable Long folderId) {
        return deckMapper.toDtoList(deckService.getDecksByFolderId(folderId));
    }

    /**
     * Create new deck.
     * POST /api/decks
     * Example JSON:
     * {
     *   "name": "My Deck",
     *   "description": "Short desc",
     *   "folderId": 1,
     *   "flashcards": [
     *     { "frontText": "A", "backText": "B" }
     *   ]
     * }
     */
    @PostMapping
    public ResponseEntity<DeckDto> createDeck(@Valid @RequestBody DeckCreateRequestDto deckDto) {
        DeckDto created = deckService.createDeck(deckDto);
        return ResponseEntity.created(URI.create("/api/decks/" + created.getId())).body(created);
    }

    /**
     * Update deck by id.
     * PUT /api/decks/{id}
     * Example JSON:
     * {
     *   "name": "Updated Deck Name",
     *   "description": "Updated desc",
     *   "folderId": 2,
     *   "flashcards": [
     *     { "frontText": "New Q", "backText": "New A" }
     *   ]
     * }
     */
    @PutMapping("/{id}")
    public ResponseEntity<DeckDto> updateDeck(@PathVariable Long id,
                                              @Valid @RequestBody DeckCreateRequestDto deckDto) {
        DeckDto updated = deckService.updateDeck(id, deckDto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete deck by id.
     * DELETE /api/decks/{id}
     * Response: { "message": "Deck with id {id} deleted." }
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteDeck(@PathVariable Long id) {
        deckService.deleteDeck(id);
        Map<String, String> resp = new HashMap<>();
        resp.put("message", "Deck with id " + id + " deleted.");
        return ResponseEntity.ok(resp);
    }
}