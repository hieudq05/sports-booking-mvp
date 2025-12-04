package com.dqhieuse.sportbookingbackend.modules.fileupload.service;

import com.dqhieuse.sportbookingbackend.common.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.max-size}")
    private Long maxSize;

    @Value("${file.extension-allowed}")
    private List<String> allowedExtensions;

    public String uploadFile(MultipartFile file) {
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Failed to upload file. File is empty.");
        }

        if (file.getSize() > maxSize) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Failed to upload file. File size exceeds the limit.");
        }

        String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        if (!allowedExtensions.contains(fileExtension)) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Failed to upload file. File extension is not allowed.");
        }

        try {
            String uniqueFileName = UUID.randomUUID() + fileExtension;

            Path root = Paths.get(uploadDir);
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }
            Path destinationFile = root.resolve(uniqueFileName);

            Files.copy(file.getInputStream(), destinationFile);

            return ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/media/")
                    .path(uniqueFileName)
                    .toUriString();

        } catch (IOException e) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to store file.");
        }
    }

    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) {
            return;
        }

        try {
            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);

            Path root = Paths.get(uploadDir).toAbsolutePath().normalize();
            Path filePath = root.resolve(fileName).normalize();

            // Prevent path traversal: ensure filePath is within root
            if (!filePath.startsWith(root)) {
                log.warn("Attempted path traversal attack with file: {}", fileName);
                throw new AppException(HttpStatus.BAD_REQUEST, "Invalid file path.");
            }
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        } catch (IOException e) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete file.");
        }
    }
}
