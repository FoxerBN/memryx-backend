package sk.foxer.flashcard.web.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sk.foxer.flashcard.domain.service.AppUserService;
import sk.foxer.flashcard.web.dto.appuser.AppUserBasicDto;
import sk.foxer.flashcard.web.mapper.appuser.AppUserBasicMapper;
import sk.foxer.flashcard.web.mapper.appuser.AppUserMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class AppUserController {
    private final AppUserService appUserService;

    @GetMapping
    public List<AppUserBasicDto> getAllUsers() {
        return appUserService.getAllUsers()
                .stream()
                .map(AppUserBasicMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AppUserBasicDto getUserById(@PathVariable Long id) {
        return AppUserBasicMapper.toDto(appUserService.getUserById(id));
    }
}