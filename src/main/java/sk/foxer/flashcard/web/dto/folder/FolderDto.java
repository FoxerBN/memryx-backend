package sk.foxer.flashcard.web.dto.folder;

import lombok.Getter;
import lombok.Setter;
import sk.foxer.flashcard.web.dto.deck.DeckSummaryDto;

import java.util.List;

@Getter
@Setter
public class FolderDto {
    private Long id;
    private String name;
    private Long userId;
    private List<DeckSummaryDto> decks;
}