package sk.foxer.flashcard.domain.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import sk.foxer.flashcard.domain.model.Folder;
import sk.foxer.flashcard.web.dto.folder.FolderSummaryDto;

import java.util.List;
import java.util.Optional;

public interface FolderRepository extends JpaRepository<Folder, Long> {

    @EntityGraph(attributePaths = {"appUser"})
    List<Folder> findAll();

    @EntityGraph(attributePaths = {"appUser"})
    Optional<Folder> findById(Long id);

    @EntityGraph(attributePaths = {"appUser"})
    List<Folder> findByAppUserId(Long appUserId);

    Folder findByNameAndAppUserId(String name, Long appUserId);

    @Query("""
        select new sk.foxer.flashcard.web.dto.folder.FolderSummaryDto(
            f.id, f.name, count(d.id))
        from Folder f
        left join f.decks d
        where (:userId is null or f.appUser.id = :userId)
        group by f.id, f.name
    """)
    List<FolderSummaryDto> findFolderSummaries(@Param("userId") Long userId);
}
