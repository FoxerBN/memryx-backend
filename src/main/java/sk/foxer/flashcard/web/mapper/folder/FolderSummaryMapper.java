package sk.foxer.flashcard.web.mapper.folder;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sk.foxer.flashcard.domain.model.Folder;
import sk.foxer.flashcard.web.dto.folder.FolderSummaryDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FolderSummaryMapper {
    @Mapping(target = "deckCount", expression = "java(folder.getDecks() != null ? folder.getDecks().size() : 0)")
    FolderSummaryDto toSummaryDto(Folder folder);

    List<FolderSummaryDto> toSummaryDtoList(List<Folder> folders);
}