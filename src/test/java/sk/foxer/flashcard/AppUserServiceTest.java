package sk.foxer.flashcard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import sk.foxer.flashcard.api.exception.ConflictException;
import sk.foxer.flashcard.api.exception.EmptyBodyException;
import sk.foxer.flashcard.api.exception.ResourceNotFoundException;
import sk.foxer.flashcard.api.exception.ValidationException;
import sk.foxer.flashcard.domain.model.AppUser;
import sk.foxer.flashcard.domain.repository.AppUserRepository;
import sk.foxer.flashcard.domain.service.AppUserService;
import sk.foxer.flashcard.web.dto.appuser.AppUserCreateRequestDto;
import sk.foxer.flashcard.web.dto.appuser.AppUserUpdateRequestDto;
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
        appUserService = new AppUserService(appUserRepository);
    }

    @Test
    void shouldGetUserById() {
        AppUser user = new AppUser(1L, "user1", "User One", null);
        when(appUserRepository.findById(1L)).thenReturn(Optional.of(user));

        AppUser result = appUserService.getUserById(1L);

        assertEquals("user1", result.getUsername());
        verify(appUserRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        when(appUserRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> appUserService.getUserById(999L));
    }

    @Test
    void shouldCreateUser() {
        AppUserCreateRequestDto dto = new AppUserCreateRequestDto();
        dto.setUsername("foxer");
        dto.setDisplayName("FoxerSK");

        AppUser user = new AppUser(null, "foxer", "FoxerSK", null);
        when(appUserBasicMapper.toEntity(dto)).thenReturn(user);
        when(appUserRepository.existsByUsername("foxer")).thenReturn(false);
        when(appUserRepository.save(user)).thenReturn(user);

        AppUser created = appUserService.createUser(dto, appUserBasicMapper);

        assertEquals("foxer", created.getUsername());
        verify(appUserRepository).save(user);
    }

    @Test
    void shouldThrowIfUsernameExists() {
        AppUserCreateRequestDto dto = new AppUserCreateRequestDto();
        dto.setUsername("foxer");
        dto.setDisplayName("FoxerSK");

        when(appUserRepository.existsByUsername("foxer")).thenReturn(true);

        assertThrows(ConflictException.class, () -> appUserService.createUser(dto, appUserBasicMapper));
    }

    @Test
    void shouldThrowIfDtoIsNull() {
        assertThrows(EmptyBodyException.class, () -> appUserService.createUser(null, appUserBasicMapper));
    }

    @Test
    void shouldThrowIfDisplayNameIsNullOrEmpty() {
        AppUserCreateRequestDto dto = new AppUserCreateRequestDto();
        dto.setUsername("foxer");
        dto.setDisplayName(null);

        when(appUserRepository.existsByUsername("foxer")).thenReturn(false);

        assertThrows(ValidationException.class, () -> appUserService.createUser(dto, appUserBasicMapper));

        dto.setDisplayName("");
        assertThrows(ValidationException.class, () -> appUserService.createUser(dto, appUserBasicMapper));
    }

    @Test
    void shouldUpdateDisplayName() {
        AppUser user = new AppUser(1L, "foxer", "OldName", null);
        when(appUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(appUserRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        AppUser updated = appUserService.updateDisplayName(1L, "NewName");

        assertEquals("NewName", updated.getDisplayName());
        verify(appUserRepository).save(user);
    }

    @Test
    void shouldThrowWhenUpdateUserNotFound() {
        when(appUserRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> appUserService.updateDisplayName(2L, "NewName"));
    }


    @Test
    void shouldDeleteUser() {
        AppUser user = new AppUser(1L, "foxer", "name", null);
        when(appUserRepository.findById(1L)).thenReturn(Optional.of(user));

        appUserService.deleteUser(1L);
        verify(appUserRepository).delete(user);
    }

    @Test
    void shouldThrowWhenDeleteUserNotFound() {
        when(appUserRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> appUserService.deleteUser(2L));
    }

    @Test
    void shouldCallBasicMapperOnCreateUser() {
        AppUserCreateRequestDto dto = new AppUserCreateRequestDto();
        dto.setUsername("foxer");
        dto.setDisplayName("FoxerSK");

        when(appUserRepository.existsByUsername("foxer")).thenReturn(false);

        appUserService.createUser(dto, appUserBasicMapper);
        verify(appUserBasicMapper).toEntity(dto);
    }

    @Test
    void shouldSaveCorrectUserOnCreate() {
        AppUserCreateRequestDto dto = new AppUserCreateRequestDto();
        dto.setUsername("testuser");
        dto.setDisplayName("Test User");

        AppUser user = new AppUser(null, "testuser", "Test User", null);
        when(appUserBasicMapper.toEntity(dto)).thenReturn(user);
        when(appUserRepository.existsByUsername("testuser")).thenReturn(false);
        when(appUserRepository.save(any())).thenReturn(user);

        appUserService.createUser(dto, appUserBasicMapper);

        ArgumentCaptor<AppUser> captor = ArgumentCaptor.forClass(AppUser.class);
        verify(appUserRepository).save(captor.capture());
        assertEquals("testuser", captor.getValue().getUsername());
        assertEquals("Test User", captor.getValue().getDisplayName());
    }
}