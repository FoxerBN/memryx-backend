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
import sk.foxer.flashcard.web.mapper.deckmapper.DeckMapper;
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
    /**
     * Retrieves all decks from the repository.
     *
     * @return a list of all decks
     */
    public List<Deck> getAllDecks() {
        return deckRepository.findAll();
    }


    /**
     * Retrieves a deck by its unique identifier.
     *
     * @param id the unique ID of the deck
     * @return the Deck instance
     * @throws ResourceNotFoundException if the deck is not found
     */
    public Deck getDeckById(Long id) {
        return deckRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deck not found with id: " + id));
    }

    /**
     * Retrieves all decks associated with a specific user.
     *
     * @param userId the ID of the user
     * @return a list of Decks associated with the user
     * @throws ResourceNotFoundException if the user is not found
     */
    public List<Deck> getAllDeckByUser(Long userId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return deckRepository.findByAppUserId(user.getId());
    }

    /**
     * Creates a new deck for a user.
     *
     * @param userId the ID of the user creating the deck
     * @param dto    the request DTO containing deck details
     * @return the created Deck DTO
     * @throws ValidationException if the request body is empty or invalid
     */
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

    /**
     * Deletes a deck by its unique identifier.
     *
     * @param id the unique ID of the deck to delete
     * @throws ResourceNotFoundException if the deck is not found
     */
    public void deleteDeck(Long id){
        Deck deck = deckRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deck not found with id: " + id));
        deckRepository.delete(deck);
    }

    /**
     * Updates an existing deck with new details.
     *
     * @param id  the unique ID of the deck to update
     * @param dto the request DTO containing updated deck details
     * @return the updated Deck DTO
     * @throws ResourceNotFoundException if the deck is not found
     * @throws ValidationException if the request body is empty or invalid
     */
    public DeckDto updateDeck(Long id, DeckCreateRequestDto dto) {
        Deck deck = deckRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deck not found with id: " + id));

        if (dto == null) {throw new ValidationException("Request body is empty");}
        if (dto.getFlashcards() == null || dto.getFlashcards().isEmpty()) {throw new ValidationException("At least one flashcard is required");}
        if (dto.getFlashcards().size() > 100) {throw new ValidationException("Max 100 flashcards per deck");}

        List<Flashcard> flashcards = dto.getFlashcards().stream()
                .map(flashcardDto -> {
                    Flashcard flashcard = new Flashcard();
                    flashcard.setFrontText(flashcardDto.getFrontText());
                    flashcard.setBackText(flashcardDto.getBackText());
                    flashcard.setDeck(deck);
                    return flashcard;
                })
                .toList();

        deck.setName(dto.getName());
        deck.setDescription(dto.getDescription());
        deck.getFlashcards().clear();
        deck.getFlashcards().addAll(flashcards);

        Deck saved = deckRepository.save(deck);
        return deckMapper.toDto(saved);
    }
}
