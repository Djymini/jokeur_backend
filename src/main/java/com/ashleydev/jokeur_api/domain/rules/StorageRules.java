package com.ashleydev.jokeur_api.domain.rules;

import com.ashleydev.jokeur_api.exceptions.storage.FileStorageException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
public class StorageRules {

  public void validateFile(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new FileStorageException("File is empty", null);
    }

    String contentType = file.getContentType();
    if (contentType == null || !contentType.startsWith("image/")) {
      throw new FileStorageException("File must be an image", null);
    }

    String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
    if (extension == null) {
      throw new FileStorageException("Invalid file extension", null);
    }
  }
}
