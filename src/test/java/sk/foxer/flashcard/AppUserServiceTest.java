package sk.foxer.flashcard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.foxer.flashcard.api.exception.ConflictException;
import sk.foxer.flashcard.api.exception.EmptyBodyException;
import sk.foxer.flashcard.api.exception.ResourceNotFoundException;
import sk.foxer.flashcard.api.exception.ValidationException;
import sk.foxer.flashcard.domain.model.AppUser;
import sk.foxer.flashcard.domain.repository.AppUserRepository;
import sk.foxer.flashcard.domain.service.AppUserService;
import sk.foxer.flashcard.web.dto.appuser.AppUserBasicDto;
import sk.foxer.flashcard.web.dto.appuser.AppUserCreateRequestDto;
import sk.foxer.flashcard.web.mapper.appuser.AppUserBasicMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppUserServiceTest {

    private AppUserRepository appUserRepository;
    private AppUserBasicMapper appUserBasicMapper;
    private AppUserService appUserService;

    @BeforeEach
    void setUp() {
        appUserRepository = mock(AppUserRepository.class);
        appUserBasicMapper = mock(AppUserBasicMapper.class);
        appUserService = new AppUserService(appUserRepository, appUserBasicMapper);
    }

    @Test
    void shouldGetUserById() {
        var user = new AppUser(1L, "user1", "User One", null);
        when(appUserRepository.findById(1L)).thenReturn(Optional.of(user));

        var dto = new AppUserBasicDto();
        dto.setId(1L); dto.setUsername("user1"); dto.setDisplayName("User One");
        when(appUserBasicMapper.toDto(user)).thenReturn(dto);

        var result = appUserService.getUserById(1L);

        assertEquals("user1", result.getUsername());
        verify(appUserRepository).findById(1L);
        verify(appUserBasicMapper).toDto(user);
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        when(appUserRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> appUserService.getUserById(999L));
    }

    @Test
    void shouldCreateUser() {
        var dtoIn = new AppUserCreateRequestDto();
        dtoIn.setUsername("foxer");
        dtoIn.setDisplayName("FoxerSK");

        var entity = new AppUser(null, "foxer", "FoxerSK", null);
        when(appUserRepository.existsByUsername("foxer")).thenReturn(false);
        when(appUserBasicMapper.toEntity(dtoIn)).thenReturn(entity);
        when(appUserRepository.save(entity)).thenReturn(entity);

        var dtoOut = new AppUserBasicDto();
        dtoOut.setId(1L); dtoOut.setUsername("foxer"); dtoOut.setDisplayName("FoxerSK");
        when(appUserBasicMapper.toDto(entity)).thenReturn(dtoOut);

        var created = appUserService.createUser(dtoIn);

        assertEquals("foxer", created.getUsername());
        verify(appUserBasicMapper).toEntity(dtoIn);
        verify(appUserRepository).save(entity);
        verify(appUserBasicMapper).toDto(entity);
    }

    @Test
    void shouldThrowIfUsernameExists() {
        var dto = new AppUserCreateRequestDto();
        dto.setUsername("foxer");
        dto.setDisplayName("FoxerSK");

        when(appUserRepository.existsByUsername("foxer")).thenReturn(true);

        assertThrows(ConflictException.class, () -> appUserService.createUser(dto));
    }

    @Test
    void shouldThrowIfDtoIsNull() {
        assertThrows(EmptyBodyException.class, () -> appUserService.createUser(null));
    }

    @Test
    void shouldThrowIfDisplayNameIsNullOrEmpty() {
        var dto = new AppUserCreateRequestDto();
        dto.setUsername("foxer");
        dto.setDisplayName(null);
        when(appUserRepository.existsByUsername("foxer")).thenReturn(false);
        assertThrows(ValidationException.class, () -> appUserService.createUser(dto));

        dto.setDisplayName("");
        assertThrows(ValidationException.class, () -> appUserService.createUser(dto));
    }

    @Test
    void shouldUpdateDisplayName() {
        var user = new AppUser(1L, "foxer", "OldName", null);
        when(appUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(appUserRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var out = new AppUserBasicDto();
        out.setId(1L); out.setUsername("foxer"); out.setDisplayName("NewName");
        when(appUserBasicMapper.toDto(user)).thenReturn(out);

        var updated = appUserService.updateDisplayName(1L, "NewName");

        assertEquals("NewName", updated.getDisplayName());
        verify(appUserRepository).save(user);
        verify(appUserBasicMapper).toDto(user);
    }

    @Test
    void shouldThrowWhenUpdateUserNotFound() {
        when(appUserRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> appUserService.updateDisplayName(2L, "NewName"));
    }

    @Test
    void shouldDeleteUser() {
        var user = new AppUser(1L, "foxer", "name", null);
        when(appUserRepository.findById(1L)).thenReturn(Optional.of(user));

        appUserService.deleteUser(1L);

        verify(appUserRepository).delete(user);
    }

    @Test
    void shouldThrowWhenDeleteUserNotFound() {
        when(appUserRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> appUserService.deleteUser(2L));
    }
}
