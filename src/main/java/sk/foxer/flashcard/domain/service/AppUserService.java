package sk.foxer.flashcard.domain.service;

import org.springframework.stereotype.Service;
import sk.foxer.flashcard.api.exception.ConflictException;
import sk.foxer.flashcard.api.exception.EmptyBodyException;
import sk.foxer.flashcard.api.exception.ResourceNotFoundException;
import sk.foxer.flashcard.api.exception.ValidationException;
import sk.foxer.flashcard.domain.model.AppUser;
import sk.foxer.flashcard.domain.repository.AppUserRepository;
import sk.foxer.flashcard.web.dto.appuser.AppUserCreateRequestDto;
import sk.foxer.flashcard.web.mapper.appuser.AppUserBasicMapper;

import java.util.List;

/**
 * Service class for handling application user business logic.
 * <p>
 * Provides methods for listing, retrieving, and creating users.
 */
@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;

    /**
     * Constructs a new AppUserService with the provided repository.
     *
     * @param appUserRepository the user repository
     */
    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    /**
     * Retrieves all users from the repository.
     *
     * @return a list of all users
     */
    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the user's ID
     * @return the AppUser instance
     * @throws ResourceNotFoundException if the user is not found
     */
    public AppUser getUserById(Long id) {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
    }

    /**
     * Creates a new user.
     *
     * @param dto    the data for the new user
     * @param mapper mapper for converting DTO to entity
     * @return the saved AppUser instance
     * @throws EmptyBodyException if dto is null
     * @throws ValidationException if username is missing or blank
     * @throws ConflictException if username already exists
     */
    public AppUser createUser(AppUserCreateRequestDto dto, AppUserBasicMapper mapper) {
        if (dto == null) {
            throw new EmptyBodyException("Request body is empty");
        }
        if (dto.getUsername() == null || dto.getUsername().isBlank()) {
            throw new ValidationException("Username must not be null or blank");
        }
        if (appUserRepository.existsByUsername(dto.getUsername())) {
            throw new ConflictException("Username already exists");
        }
        AppUser user = mapper.toEntity(dto);
        return appUserRepository.save(user);
    }
}
