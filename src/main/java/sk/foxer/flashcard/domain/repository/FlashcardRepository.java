package sk.foxer.flashcard.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.foxer.flashcard.domain.model.Flashcard;

public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {
}
