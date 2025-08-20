package sk.foxer.flashcard.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeckService {

    private final DeckRepository deckRepository;
    private final FolderRepository folderRepository;
    private final DeckMapper deckMapper;

    public List<DeckDto> getAllDecks() {
        var decks = deckRepository.findAll();           // @EntityGraph(folder) v repo
        return deckMapper.toDtoList(decks);
    }

    public DeckDto getDeckById(Long id) {
        Deck deck = deckRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deck not found with id: " + id));
        return deckMapper.toDto(deck);
    }

    public List<DeckDto> getDecksByFolderId(Long folderId) {
        // validácia existencie foldera (voliteľná – máš ju nižšie, nechávam rýchlu verziu)
        var decks = deckRepository.findByFolderId(folderId); // @EntityGraph(folder)
        return deckMapper.toDtoList(decks);
    }

    public List<DeckSummaryDto> getAllDeckSummaries() {
        return deckRepository.findDeckSummaries(null);
    }

    public List<DeckSummaryDto> getDeckSummariesByFolder(Long folderId) {
        // Ak chceš prísnu validáciu, nechaj aj kontrolu existencie foldera:
        folderRepository.findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found with id: " + folderId));
        return deckRepository.findDeckSummaries(folderId);
    }

    @Transactional
    public DeckDto createDeck(DeckCreateRequestDto dto) {
        if (dto == null) throw new ValidationException("Request body is empty");
        if (dto.getFolderId() == null) throw new ValidationException("Folder ID is required");
        if (dto.getFlashcards() == null || dto.getFlashcards().isEmpty())
            throw new ValidationException("At least one flashcard is required");

        Folder folder = folderRepository.findById(dto.getFolderId())
                .orElseThrow(() -> new ValidationException("Folder not found with id: " + dto.getFolderId()));

        // deck z mappera – bez vzťahov
        Deck deck = deckMapper.toEntity(dto);
        deck.setFolder(folder);

        // → vytvor entitné flashcards a nastav obojsmerný vzťah
        List<Flashcard> flashcards = dto.getFlashcards().stream().map(fDto -> {
            Flashcard f = new Flashcard();
            f.setFrontText(fDto.getFrontText());
            f.setBackText(fDto.getBackText());
            f.setDeck(deck);
            return f;
        }).toList();

        // ak list v entite nie je inicializovaný, inicializuj
        if (deck.getFlashcards() == null) {
            deck.setFlashcards(new ArrayList<>());
        }
        deck.getFlashcards().clear();
        deck.getFlashcards().addAll(flashcards);

        Deck saved = deckRepository.save(deck);
        return deckMapper.toDto(saved);
    }


    @Transactional
    public DeckDto updateDeck(Long id, DeckCreateRequestDto dto) {
        if (dto == null) throw new ValidationException("Request body is empty");
        if (dto.getFlashcards() == null || dto.getFlashcards().isEmpty()) {
            throw new ValidationException("At least one flashcard is required");
        }
        if (dto.getFlashcards().size() > 100) {
            throw new ValidationException("Max 100 flashcards per deck");
        }

        Deck deck = deckRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deck not found with id: " + id));

        if (dto.getFolderId() != null && !dto.getFolderId().equals(deck.getFolder().getId())) {
            Folder newFolder = folderRepository.findById(dto.getFolderId())
                    .orElseThrow(() -> new ValidationException("Folder not found with id: " + dto.getFolderId()));
            deck.setFolder(newFolder);
        }

        List<Flashcard> flashcards = dto.getFlashcards().stream()
                .map(flashcardDto -> {
                    Flashcard f = new Flashcard();
                    f.setFrontText(flashcardDto.getFrontText());
                    f.setBackText(flashcardDto.getBackText());
                    f.setDeck(deck);
                    return f;
                })
                .toList();

        deck.setName(dto.getName());
        deck.setDescription(dto.getDescription());

        if (deck.getFlashcards() == null) {
            deck.setFlashcards(new ArrayList<>());
        }
        deck.getFlashcards().clear();
        deck.getFlashcards().addAll(flashcards);

        Deck saved = deckRepository.save(deck);
        return deckMapper.toDto(saved);
    }

    @Transactional
    public void deleteDeck(Long id) {
        Deck deck = deckRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deck not found with id: " + id));
        deckRepository.delete(deck);
    }
}
