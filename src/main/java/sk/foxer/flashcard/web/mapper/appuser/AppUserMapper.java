package sk.foxer.flashcard.web.mapper.appuser;

import org.mapstruct.Mapper;
import sk.foxer.flashcard.domain.model.AppUser;
import sk.foxer.flashcard.web.dto.appuser.AppUserDto;
import sk.foxer.flashcard.web.mapper.folder.FolderSummaryMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = { FolderSummaryMapper.class })
public interface AppUserMapper {
    AppUserDto toDto(AppUser user);
    AppUser toEntity(AppUserDto dto);
    List<AppUserDto> toDtoList(List<AppUser> users);
}