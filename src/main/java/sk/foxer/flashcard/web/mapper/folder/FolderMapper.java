package sk.foxer.flashcard.web.mapper.folder;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sk.foxer.flashcard.domain.model.Folder;
import sk.foxer.flashcard.web.dto.folder.FolderCreateRequestDto;
import sk.foxer.flashcard.web.dto.folder.FolderDto;
import sk.foxer.flashcard.web.mapper.deckmapper.DeckSummaryMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DeckSummaryMapper.class})
public interface FolderMapper {
    @Mapping(target = "decks", source = "decks")
    FolderDto toDto(Folder folder);
    List<FolderDto> toDtoList(List<Folder> folders);
    Folder toEntity(FolderCreateRequestDto dto);
}