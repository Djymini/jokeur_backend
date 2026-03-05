package com.ashleydev.jokeur_api.domain.services;

import com.ashleydev.jokeur_api.domain.rules.StorageRules;
import com.ashleydev.jokeur_api.exceptions.storage.FileStorageException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageService {

  private final StorageRules storageRules;

  @Value("${app.upload.dir}")
  private String uploadDir;

  public StorageService(StorageRules storageRules) {
    this.storageRules = storageRules;
  }

  public String store(MultipartFile file, Long healthRecordId) {
    storageRules.validateFile(file);

    try {
      String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
      String filename = UUID.randomUUID() + "." + extension;
      String relativePath = "animals/" + healthRecordId + "/" + filename;
      Path targetPath = Paths.get(uploadDir).resolve(relativePath);
      Files.createDirectories(targetPath.getParent());
      Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
      return relativePath;
    } catch (IOException e) {
      throw new FileStorageException("Error while saving file", e);
    }
  }

  public void delete(String photoKey) {
    try {
      Path path = Paths.get(uploadDir).resolve(photoKey);
      Files.deleteIfExists(path);
    } catch (IOException e) {
      throw new FileStorageException("Error while deleting file", e);
    }
  }
}
