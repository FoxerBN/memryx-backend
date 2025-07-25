package sk.foxer.flashcard.web.mapper.appuser;

import org.mapstruct.Mapper;
import sk.foxer.flashcard.domain.model.AppUser;
import sk.foxer.flashcard.web.dto.appuser.AppUserBasicDto;
import sk.foxer.flashcard.web.dto.appuser.AppUserCreateRequestDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppUserBasicMapper {
    List<AppUserBasicDto> toDtoList(List<AppUser> users);
    AppUserBasicDto toDto(AppUser user);
    AppUser toEntity(AppUserCreateRequestDto dto);
}
