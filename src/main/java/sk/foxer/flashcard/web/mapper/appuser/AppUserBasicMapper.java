package sk.foxer.flashcard.web.mapper.appuser;

import sk.foxer.flashcard.domain.model.AppUser;
import sk.foxer.flashcard.web.dto.appuser.AppUserBasicDto;

public class AppUserBasicMapper {
    public static AppUserBasicDto toDto(AppUser user) {
        if (user == null) return null;
        AppUserBasicDto dto = new AppUserBasicDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setDisplayName(user.getDisplayName());
        return dto;
    }
}
