package sk.foxer.flashcard.domain.service;

import org.springframework.stereotype.Service;
import sk.foxer.flashcard.api.exception.ResourceNotFoundException;
import sk.foxer.flashcard.api.exception.ValidationException;
import sk.foxer.flashcard.domain.model.AppUser;
import sk.foxer.flashcard.domain.model.Deck;
import sk.foxer.flashcard.domain.model.Flashcard;
import sk.foxer.flashcard.domain.repository.AppUserRepository;
import sk.foxer.flashcard.domain.repository.DeckRepository;
import sk.foxer.flashcard.web.dto.deck.DeckCreateRequestDto;
import sk.foxer.flashcard.web.dto.deck.DeckDto;
import sk.foxer.flashcard.web.dto.flashcard.FlashcardCreateRequestDto;
import sk.foxer.flashcard.web.mapper.deckmapper.DeckMapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeckService {
    private final DeckRepository deckRepository;
    private final AppUserRepository appUserRepository;
    private final DeckMapper deckMapper;

    public DeckService(DeckRepository deckRepository, AppUserRepository appUserRepository,
                       DeckMapper deckMapper) {
        this.appUserRepository = appUserRepository;
        this.deckRepository = deckRepository;
        this.deckMapper = deckMapper;
    }

    public List<Deck> getAllDecks() {
        return deckRepository.findAll();
    }

    public Deck getDeckById(Long id) {
        return deckRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deck not found with id: " + id));
    }

    public DeckDto createDeck(Long userId, DeckCreateRequestDto dto) {
        if (dto == null) throw new ValidationException("Request body is empty");
        if (dto.getFlashcards() == null || dto.getFlashcards().isEmpty()) {
            throw new ValidationException("At least one flashcard is required");
        }

        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new ValidationException("User not found"));

        Deck deck = deckMapper.toEntity(dto);
        deck.setAppUser(user);

        Deck saved = deckRepository.save(deck);
        return deckMapper.toDto(saved);
    }
}
