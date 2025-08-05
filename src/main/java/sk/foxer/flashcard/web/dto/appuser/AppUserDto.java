package sk.foxer.flashcard.web.dto.appuser;

import lombok.Getter;
import lombok.Setter;
import sk.foxer.flashcard.web.dto.folder.FolderSummaryDto;

import java.util.List;

@Getter
@Setter
public class AppUserDto {
    private Long id;
    private String username;
    private String displayName;
    private List<FolderSummaryDto> folders;
}