package sk.foxer.flashcard.web.mapper.appuser;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sk.foxer.flashcard.domain.model.AppUser;
import sk.foxer.flashcard.web.dto.appuser.AppUserDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppUserMapper {
    @Mapping(target = "folders", ignore = true) // summary sa vracia samostatným endpointom
    AppUserDto toDto(AppUser user);

    List<AppUserDto> toDtoList(List<AppUser> users);

    @Mapping(target = "folders", ignore = true)
    AppUser toEntity(AppUserDto dto);
}
