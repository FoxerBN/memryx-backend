package sk.foxer.flashcard.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.foxer.flashcard.domain.model.Deck;
import sk.foxer.flashcard.web.dto.deck.DeckDto;

import java.util.List;

public interface DeckRepository extends JpaRepository<Deck, Long> {
    List<Deck> findByFolderId(Long folderId);
}
