package sk.foxer.flashcard.web.dto.folder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FolderSummaryDto {
    private Long id;
    private String name;
    private int deckCount;
}