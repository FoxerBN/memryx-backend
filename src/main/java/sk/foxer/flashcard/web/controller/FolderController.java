package sk.foxer.flashcard.web.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.foxer.flashcard.domain.service.FolderService;
import sk.foxer.flashcard.web.dto.folder.FolderCreateRequestDto;
import sk.foxer.flashcard.web.dto.folder.FolderDto;
import sk.foxer.flashcard.web.dto.folder.FolderSummaryDto;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/folders")
public class FolderController {

    private final FolderService folderService;

    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @GetMapping
    public List<FolderDto> getAllFolders() {
        return folderService.getAllFolders();
    }

    @GetMapping("/summary")
    public List<FolderSummaryDto> getAllFolderSummaries() {
        return folderService.getAllFolderSummaries();
    }

    @GetMapping("/{id}")
    public FolderDto getFolderById(@PathVariable Long id) {
        return folderService.getFolderById(id);
    }

    @GetMapping("/user/{userId}")
    public List<FolderDto> getFoldersByUserId(@PathVariable Long userId) {
        return folderService.getFoldersByUserId(userId);
    }

    @GetMapping("/user/{userId}/summary")
    public List<FolderSummaryDto> getFolderSummariesByUserId(@PathVariable Long userId) {
        return folderService.getFolderSummariesByUserId(userId);
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<FolderDto> createFolder(@PathVariable Long userId,
                                                  @Valid @RequestBody FolderCreateRequestDto folderDto) {
        FolderDto created = folderService.createFolder(userId, folderDto);
        return ResponseEntity.created(URI.create("/api/folders/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FolderDto> updateFolder(@PathVariable Long id,
                                                  @Valid @RequestBody FolderCreateRequestDto folderDto) {
        return ResponseEntity.ok(folderService.updateFolder(id, folderDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteFolder(@PathVariable Long id) {
        folderService.deleteFolder(id);
        Map<String, String> resp = new HashMap<>();
        resp.put("message", "Folder with id " + id + " deleted.");
        return ResponseEntity.ok(resp);
    }
}
