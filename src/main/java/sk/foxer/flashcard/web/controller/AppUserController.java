package sk.foxer.flashcard.web.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sk.foxer.flashcard.domain.model.AppUser;
import sk.foxer.flashcard.domain.service.AppUserService;
import sk.foxer.flashcard.web.dto.appuser.AppUserBasicDto;
import sk.foxer.flashcard.web.dto.appuser.AppUserCreateRequestDto;
import sk.foxer.flashcard.web.mapper.appuser.AppUserBasicMapper;
import sk.foxer.flashcard.web.mapper.appuser.AppUserMapper;

import java.util.List;

/**
 * REST controller for managing application users.
 * <p>
 * Provides endpoints for listing users, fetching a user by ID,
 * and creating new users.
 */
@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;
    private final AppUserMapper appUserMapper;
    private final AppUserBasicMapper appUserBasicMapper;

    /**
     * Retrieves all users.
     *
     * @return a list of all users in basic DTO format
     */
    @GetMapping
    public List<AppUserBasicDto> getAllUsers() {
        return appUserBasicMapper.toDtoList(appUserService.getAllUsers());
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the unique ID of the user
     * @return the user's basic DTO
     * @throws sk.foxer.flashcard.api.exception.ResourceNotFoundException if user not found
     */
    @GetMapping("/{id}")
    public AppUserBasicDto getUserById(@PathVariable Long id) {
        return appUserBasicMapper.toDto(appUserService.getUserById(id));
    }

    /**
     * Creates a new user.
     *
     * @param userDto the request body containing user details
     * @return the created user's basic DTO
     * @throws sk.foxer.flashcard.api.exception.ConflictException if the username already exists
     * @throws sk.foxer.flashcard.api.exception.ValidationException if username is blank or null
     * @throws sk.foxer.flashcard.api.exception.EmptyBodyException if the request body is empty
     */
    @PostMapping("/create")
    public AppUserBasicDto createUser(@Valid @RequestBody AppUserCreateRequestDto userDto) {
        AppUser user = appUserService.createUser(userDto, appUserBasicMapper);
        return appUserBasicMapper.toDto(user);
    }
}
