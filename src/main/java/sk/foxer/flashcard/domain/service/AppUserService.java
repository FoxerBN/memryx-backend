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

@Service
public class AppUserService {
    private final AppUserRepository appUserRepository;

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    public AppUser getUserById(Long id) {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
    }

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
