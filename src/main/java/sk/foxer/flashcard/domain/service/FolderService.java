package sk.foxer.flashcard.domain.service;

import org.springframework.stereotype.Service;
import sk.foxer.flashcard.api.exception.ResourceNotFoundException;
import sk.foxer.flashcard.api.exception.ValidationException;
import sk.foxer.flashcard.domain.model.AppUser;
import sk.foxer.flashcard.domain.model.Folder;
import sk.foxer.flashcard.domain.repository.AppUserRepository;
import sk.foxer.flashcard.domain.repository.FolderRepository;
import sk.foxer.flashcard.web.dto.folder.FolderCreateRequestDto;
import sk.foxer.flashcard.web.dto.folder.FolderDto;
import sk.foxer.flashcard.web.dto.folder.FolderSummaryDto;
import sk.foxer.flashcard.web.mapper.folder.FolderMapper;
import sk.foxer.flashcard.web.mapper.folder.FolderSummaryMapper;

import java.util.List;

/**
 * Service for managing folders (CRUD + user filtering + summary).
 */
@Service
public class FolderService {
    private final FolderRepository folderRepository;
    private final AppUserRepository appUserRepository;
    private final FolderMapper folderMapper;
    private final FolderSummaryMapper folderSummaryMapper;

    public FolderService(FolderRepository folderRepository,
                         AppUserRepository appUserRepository,
                         FolderMapper folderMapper,
                         FolderSummaryMapper folderSummaryMapper) {
        this.folderRepository = folderRepository;
        this.appUserRepository = appUserRepository;
        this.folderMapper = folderMapper;
        this.folderSummaryMapper = folderSummaryMapper;
    }

    /** Get all folders (entities). */
    public List<Folder> getAllFolders() {
        return folderRepository.findAll();
    }

    /** Get folder by ID (entity). */
    public Folder getFolderById(Long id) {
        return folderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found with id: " + id));
    }

    /** Get all folders owned by a user (entities). */
    public List<Folder> getFoldersByUserId(Long userId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return folderRepository.findByAppUserId(user.getId());
    }

    /** Get all folders owned by a user, as summary DTOs. */
    public List<FolderSummaryDto> getFolderSummariesByUserId(Long userId) {
        List<Folder> folders = getFoldersByUserId(userId);
        return folderSummaryMapper.toSummaryDtoList(folders);
    }

    /** Get all folders as summary DTOs. */
    public List<FolderSummaryDto> getAllFolderSummaries() {
        List<Folder> folders = folderRepository.findAll();
        return folderSummaryMapper.toSummaryDtoList(folders);
    }

    /** Create a new folder for a user. */
    public FolderDto createFolder(Long userId, FolderCreateRequestDto dto) {
        if (dto == null) throw new ValidationException("Request body is empty");
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new ValidationException("Folder name is required");
        }

        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Folder existingFolder = folderRepository.findByNameAndAppUserId(dto.getName(), userId);
        if (existingFolder != null) {
            throw new ValidationException("Folder with this name already exists for user");
        }

        Folder folder = folderMapper.toEntity(dto);
        folder.setAppUser(user);

        Folder saved = folderRepository.save(folder);
        return folderMapper.toDto(saved);
    }

    /** Delete folder by id */
    public void deleteFolder(Long id) {
        Folder folder = folderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found with id: " + id));
        folderRepository.delete(folder);
    }

    /** Update folder (name) */
    public FolderDto updateFolder(Long id, FolderCreateRequestDto dto) {
        Folder folder = folderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found with id: " + id));

        if (dto == null) throw new ValidationException("Request body is empty");
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new ValidationException("Folder name is required");
        }

        Folder existingFolder = folderRepository.findByNameAndAppUserId(dto.getName(), folder.getAppUser().getId());
        if (existingFolder != null && !existingFolder.getId().equals(id)) {
            throw new ValidationException("Folder with this name already exists for user");
        }

        folder.setName(dto.getName());
        Folder saved = folderRepository.save(folder);
        return folderMapper.toDto(saved);
    }
}