package sk.foxer.flashcard.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sk.foxer.flashcard.domain.model.Folder;
import sk.foxer.flashcard.domain.service.FolderService;
import sk.foxer.flashcard.web.dto.folder.FolderCreateRequestDto;
import sk.foxer.flashcard.web.dto.folder.FolderDto;
import sk.foxer.flashcard.web.mapper.folder.FolderMapper;
import sk.foxer.flashcard.web.mapper.folder.FolderSummaryMapper;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/folders")
public class FolderController {

    private final FolderService folderService;
    private final FolderMapper folderMapper;

    public FolderController(FolderService folderService, FolderMapper folderMapper) {
        this.folderService = folderService;
        this.folderMapper = folderMapper;
    }

    @GetMapping("/test")
    public List<Map<String, Object>> testFolders() {
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> folder = new HashMap<>();
        folder.put("id", 1);
        folder.put("name", "Test Folder");
        result.add(folder);
        return result;
    }

    @GetMapping
    public List<FolderDto> getAllFolders() {
        return folderMapper.toDtoList(folderService.getAllFolders());
    }

    @GetMapping("/{id}")
    public FolderDto getFolderById(@PathVariable Long id) {
        return folderMapper.toDto(folderService.getFolderById(id));
    }

    @PostMapping("/user/{userId}")
    public FolderDto createFolder(@PathVariable Long userId, @Valid @RequestBody FolderCreateRequestDto folderDto) {
        return folderService.createFolder(userId, folderDto);
    }

    @DeleteMapping("/{id}")
    public void deleteFolder(@PathVariable Long id) {
        folderService.deleteFolder(id);
    }
}