package sk.foxer.flashcard.web.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.foxer.flashcard.domain.service.FolderService;
import sk.foxer.flashcard.web.dto.folder.FolderCreateRequestDto;
import sk.foxer.flashcard.web.dto.folder.FolderDto;
import sk.foxer.flashcard.web.dto.folder.FolderSummaryDto;
import sk.foxer.flashcard.web.mapper.folder.FolderMapper;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST controller for folder management.
 */
@RestController
@RequestMapping("/api/folders")
public class FolderController {

    private final FolderService folderService;
    private final FolderMapper folderMapper;

    public FolderController(FolderService folderService, FolderMapper folderMapper) {
        this.folderService = folderService;
        this.folderMapper = folderMapper;
    }

    /** Get all folders as DTOs. */
    @GetMapping
    public List<FolderDto> getAllFolders() {
        return folderMapper.toDtoList(folderService.getAllFolders());
    }

    /** Get all folders as summary DTOs. */
    @GetMapping("/summary")
    public List<FolderSummaryDto> getAllFolderSummaries() {
        return folderService.getAllFolderSummaries();
    }

    /** Get folder by id. */
    @GetMapping("/{id}")
    public FolderDto getFolderById(@PathVariable Long id) {
        return folderMapper.toDto(folderService.getFolderById(id));
    }

    /** Get all folders for a user as DTOs. */
    @GetMapping("/user/{userId}")
    public List<FolderDto> getFoldersByUserId(@PathVariable Long userId) {
        return folderMapper.toDtoList(folderService.getFoldersByUserId(userId));
    }

    /** Get all folders for a user as summary DTOs. */
    @GetMapping("/user/{userId}/summary")
    public List<FolderSummaryDto> getFolderSummariesByUserId(@PathVariable Long userId) {
        return folderService.getFolderSummariesByUserId(userId);
    }

    /** Create folder for a user. */
    @PostMapping("/user/{userId}")
    public ResponseEntity<FolderDto> createFolder(@PathVariable Long userId,
                                                  @Valid @RequestBody FolderCreateRequestDto folderDto) {
        FolderDto created = folderService.createFolder(userId, folderDto);
        return ResponseEntity.created(URI.create("/api/folders/" + created.getId())).body(created);
    }

    /** Update folder. */
    @PutMapping("/{id}")
    public ResponseEntity<FolderDto> updateFolder(@PathVariable Long id,
                                                  @Valid @RequestBody FolderCreateRequestDto folderDto) {
        FolderDto updated = folderService.updateFolder(id, folderDto);
        return ResponseEntity.ok(updated);
    }

    /** Delete folder. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteFolder(@PathVariable Long id) {
        folderService.deleteFolder(id);
        Map<String, String> resp = new HashMap<>();
        resp.put("message", "Folder with id " + id + " deleted.");
        return ResponseEntity.ok(resp);
    }
}