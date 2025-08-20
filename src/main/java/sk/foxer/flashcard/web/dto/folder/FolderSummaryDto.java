package sk.foxer.flashcard.web.dto.folder;

import lombok.Getter;

@Getter
public class FolderSummaryDto {
    private Long id;
    private String name;
    private long deckCount;


    public FolderSummaryDto(Long id, String name, long deckCount) {
        this.id = id;
        this.name = name;
        this.deckCount = deckCount;
    }

}