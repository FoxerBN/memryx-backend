package sk.foxer.flashcard.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.foxer.flashcard.api.exception.ConflictException;
import sk.foxer.flashcard.api.exception.EmptyBodyException;
import sk.foxer.flashcard.api.exception.ResourceNotFoundException;
import sk.foxer.flashcard.api.exception.ValidationException;
import sk.foxer.flashcard.domain.model.AppUser;
import sk.foxer.flashcard.domain.repository.AppUserRepository;
import sk.foxer.flashcard.web.dto.appuser.AppUserBasicDto;
import sk.foxer.flashcard.web.dto.appuser.AppUserCreateRequestDto;
import sk.foxer.flashcard.web.mapper.appuser.AppUserBasicMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final AppUserBasicMapper appUserBasicMapper;

    public List<AppUserBasicDto> getAllUsers() {
        return appUserBasicMapper.toDtoList(appUserRepository.findAll());
    }

    public AppUserBasicDto getUserById(Long id) {
        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        return appUserBasicMapper.toDto(user);
    }

    @Transactional
    public AppUserBasicDto createUser(AppUserCreateRequestDto dto) {
        if (dto == null) throw new EmptyBodyException("Request body is empty");
        if (dto.getUsername() == null || dto.getUsername().isBlank())
            throw new ValidationException("Username must not be null or blank");
        if (dto.getDisplayName() == null || dto.getDisplayName().isBlank())
            throw new ValidationException("Display name must not be null or blank");

        if (appUserRepository.existsByUsername(dto.getUsername()))
            throw new ConflictException("Username already exists");

        AppUser user = appUserBasicMapper.toEntity(dto);
        AppUser saved = appUserRepository.save(user);
        return appUserBasicMapper.toDto(saved);
    }

    @Transactional
    public void deleteUser(Long id){
        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        appUserRepository.delete(user);
    }

    @Transactional
    public AppUserBasicDto updateDisplayName(Long id, String displayName) {
        if (displayName == null || displayName.isBlank())
            throw new ValidationException("Display name must not be null or blank");

        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));

        user.setDisplayName(displayName);
        AppUser saved = appUserRepository.save(user);
        return appUserBasicMapper.toDto(saved);
    }
}
