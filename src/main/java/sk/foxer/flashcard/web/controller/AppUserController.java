package sk.foxer.flashcard.web.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sk.foxer.flashcard.domain.service.AppUserService;
import sk.foxer.flashcard.web.dto.appuser.AppUserBasicDto;
import sk.foxer.flashcard.web.dto.appuser.AppUserDto;
import sk.foxer.flashcard.web.mapper.appuser.AppUserBasicMapper;
import sk.foxer.flashcard.web.mapper.appuser.AppUserMapper;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class AppUserController {
    private final AppUserService appUserService;
    private final AppUserMapper appUserMapper;
    private final AppUserBasicMapper appUserBasicMapper;

    @GetMapping
    public List<AppUserBasicDto> getAllUsers() {
        return appUserBasicMapper.toDtoList(appUserService.getAllUsers());
    }

    @GetMapping("/{id}")
    public AppUserBasicDto getUserById(@PathVariable Long id) {
        return appUserBasicMapper.toDto(appUserService.getUserById(id));
    }
}
