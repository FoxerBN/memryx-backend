package sk.foxer.flashcard.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.foxer.flashcard.domain.model.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    boolean existsByUsername(String username);
    AppUser findByUsername(String username);
}
