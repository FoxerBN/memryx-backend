package sk.foxer.flashcard.domain.service;

import org.springframework.stereotype.Service;
import sk.foxer.flashcard.api.exception.ResourceNotFoundException;
import sk.foxer.flashcard.api.exception.ValidationException;
import sk.foxer.flashcard.domain.model.Deck;
import sk.foxer.flashcard.domain.model.Flashcard;
import sk.foxer.flashcard.domain.model.Folder;
import sk.foxer.flashcard.domain.repository.DeckRepository;
import sk.foxer.flashcard.domain.repository.FolderRepository;
import sk.foxer.flashcard.web.dto.deck.DeckCreateRequestDto;
import sk.foxer.flashcard.web.dto.deck.DeckDto;
import sk.foxer.flashcard.web.dto.deck.DeckSummaryDto;
import sk.foxer.flashcard.web.mapper.deckmapper.DeckMapper;
import sk.foxer.flashcard.web.mapper.deckmapper.DeckSummaryMapper;

import java.util.List;

/**
 * Service for deck management logic: create, update, delete, fetch decks.
 */
@Service
public class DeckService {
    private final DeckRepository deckRepository;
    private final FolderRepository folderRepository;
    private final DeckMapper deckMapper;
    private final DeckSummaryMapper deckSummaryMapper;

    public DeckService(DeckRepository deckRepository, FolderRepository folderRepository,
                       DeckMapper deckMapper, DeckSummaryMapper deckSummaryMapper) {
        this.deckRepository = deckRepository;
        this.folderRepository = folderRepository;
        this.deckMapper = deckMapper;
        this.deckSummaryMapper = deckSummaryMapper;
    }

    /**
     * Returns all decks in the system.
     */
    public List<Deck> getAllDecks() {
        return deckRepository.findAll();
    }

    /**
     * Returns a deck by its id or throws if not found.
     */
    public Deck getDeckById(Long id) {
        return deckRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deck not found with id: " + id));
    }


    /**
     * Returns all deck summaries (name, description, flashcard count).
     */
    public List<DeckSummaryDto> getAllDeckSummaries() {
        return deckSummaryMapper.toSummaryDtoList(deckRepository.findAll());
    }

    /**
     * Returns deck summaries for a specific folder.
     * @param folderId the folder id
     * @return list of deck summaries in the folder
     */
    public List<DeckSummaryDto> getDeckSummariesByFolder(Long folderId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found with id: " + folderId));
        return deckSummaryMapper.toSummaryDtoList(deckRepository.findByFolderId(folderId));
    }

    /**
     * Returns all decks in a specific folder.
     */
    public List<Deck> getDecksByFolderId(Long folderId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found with id: " + folderId));
        return deckRepository.findByFolderId(folder.getId());
    }

    /**
     * Creates a new deck with flashcards in a folder.
     * @param dto request data (must include flashcards and folderId)
     * @return created deck as DTO
     */
    public DeckDto createDeck(DeckCreateRequestDto dto) {
        if (dto == null) throw new ValidationException("Request body is empty");
        if (dto.getFlashcards() == null || dto.getFlashcards().isEmpty()) {
            throw new ValidationException("At least one flashcard is required");
        }
        if (dto.getFolderId() == null) {
            throw new ValidationException("Folder ID is required");
        }

        Folder folder = folderRepository.findById(dto.getFolderId())
                .orElseThrow(() -> new ValidationException("Folder not found with id: " + dto.getFolderId()));

        Deck deck = deckMapper.toEntity(dto);
        deck.setFolder(folder);

        Deck saved = deckRepository.save(deck);
        return deckMapper.toDto(saved);
    }

    /**
     * Deletes a deck by id.
     */
    public void deleteDeck(Long id) {
        Deck deck = deckRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deck not found with id: " + id));
        deckRepository.delete(deck);
    }

    /**
     * Updates a deck (name, description, flashcards, folder).
     * @param id deck id
     * @param dto new data
     * @return updated deck as DTO
     */
    public DeckDto updateDeck(Long id, DeckCreateRequestDto dto) {
        Deck deck = deckRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deck not found with id: " + id));

        if (dto == null) {
            throw new ValidationException("Request body is empty");
        }
        if (dto.getFlashcards() == null || dto.getFlashcards().isEmpty()) {
            throw new ValidationException("At least one flashcard is required");
        }
        if (dto.getFlashcards().size() > 100) {
            throw new ValidationException("Max 100 flashcards per deck");
        }
        if (dto.getFolderId() != null && !dto.getFolderId().equals(deck.getFolder().getId())) {
            Folder newFolder = folderRepository.findById(dto.getFolderId())
                    .orElseThrow(() -> new ValidationException("Folder not found with id: " + dto.getFolderId()));
            deck.setFolder(newFolder);
        }

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