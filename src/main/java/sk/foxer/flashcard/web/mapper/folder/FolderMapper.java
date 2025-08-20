package sk.foxer.flashcard.web.mapper.folder;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sk.foxer.flashcard.domain.model.Folder;
import sk.foxer.flashcard.web.dto.folder.FolderCreateRequestDto;
import sk.foxer.flashcard.web.dto.folder.FolderDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FolderMapper {

    @Mapping(target = "userId", source = "appUser.id")
    @Mapping(target = "decks", ignore = true) // ak FolderDto nemá pole 'decks', toto odpadá
    FolderDto toDto(Folder folder);

    List<FolderDto> toDtoList(List<Folder> folders);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "appUser", ignore = true)
    @Mapping(target = "decks", ignore = true)
    Folder toEntity(FolderCreateRequestDto dto);
}
