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
import sk.foxer.flashcard.web.mapper.folder.FolderMapper;

import java.util.List;

@Service
public class FolderService {
    private final FolderRepository folderRepository;
    private final AppUserRepository appUserRepository;
    private final FolderMapper folderMapper;

    public FolderService(FolderRepository folderRepository,
                         AppUserRepository appUserRepository,
                         FolderMapper folderMapper) {
        this.folderRepository = folderRepository;
        this.appUserRepository = appUserRepository;
        this.folderMapper = folderMapper;
    }

    public List<Folder> getAllFolders() {
        return folderRepository.findAll();
    }

    public Folder getFolderById(Long id) {
        return folderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found with id: " + id));
    }

    public List<Folder> getFoldersByUserId(Long userId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return folderRepository.findByAppUserId(user.getId());
    }

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

    public void deleteFolder(Long id) {
        Folder folder = folderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found with id: " + id));
        folderRepository.delete(folder);
    }

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