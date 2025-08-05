package sk.foxer.flashcard.web.dto.folder;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FolderCreateRequestDto {
    @NotBlank(message = "Folder name must not be blank")
    private String name;
}