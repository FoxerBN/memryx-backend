package sk.foxer.flashcard.web.mapper.appuser;

import sk.foxer.flashcard.domain.model.AppUser;
import sk.foxer.flashcard.web.dto.appuser.AppUserDto;
import sk.foxer.flashcard.web.mapper.DeckMapper;

import java.util.stream.Collectors;

public class AppUserMapper {

    public static AppUserDto toDto(AppUser user) {
        if (user == null) return null;
        AppUserDto dto = new AppUserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setDisplayName(user.getDisplayName());

        if (user.getDecks() != null) {
            dto.setDecks(user.getDecks()
                    .stream()
                    .map(DeckMapper::toDto)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    public static AppUser toEntity(AppUserDto dto) {
        if (dto == null) return null;
        AppUser user = new AppUser();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setDisplayName(dto.getDisplayName());
        // Decky na entitu zväčša nemapuješ automaticky, to riešiš cez service, keď ich vytváraš
        return user;
    }
}
