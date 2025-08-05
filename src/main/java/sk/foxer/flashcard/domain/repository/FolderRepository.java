package sk.foxer.flashcard.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.foxer.flashcard.domain.model.Folder;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    List<Folder> findByAppUserId(Long appUserId);
    Folder findByNameAndAppUserId(String name, Long appUserId);
}