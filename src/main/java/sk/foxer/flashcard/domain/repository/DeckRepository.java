package sk.foxer.flashcard.domain.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import sk.foxer.flashcard.domain.model.Deck;
import sk.foxer.flashcard.web.dto.deck.DeckSummaryDto;

import java.util.List;
import java.util.Optional;

public interface DeckRepository extends JpaRepository<Deck, Long> {

    @EntityGraph(attributePaths = {"folder"})
    List<Deck> findAll();

    @EntityGraph(attributePaths = {"folder"})
    Optional<Deck> findById(Long id);

    @EntityGraph(attributePaths = {"folder"})
    List<Deck> findByFolderId(Long folderId);

    @Query("""
        select new sk.foxer.flashcard.web.dto.deck.DeckSummaryDto(
            d.id, d.name, d.description, count(f.id))
        from Deck d
        left join d.flashcards f
        where (:folderId is null or d.folder.id = :folderId)
        group by d.id, d.name, d.description
    """)
    List<DeckSummaryDto> findDeckSummaries(@Param("folderId") Long folderId);
}
