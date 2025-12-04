package com.dqhieuse.sportbookingbackend.modules.fileupload.controller;

import com.dqhieuse.sportbookingbackend.common.dto.ApiResponse;
import com.dqhieuse.sportbookingbackend.modules.fileupload.dto.FileUploadResponse;
import com.dqhieuse.sportbookingbackend.modules.fileupload.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
class FileUploadController {

    private final FileUploadService fileUploadService;

    @PostMapping("/upload")
    public ApiResponse<FileUploadResponse> uploadFile(
            @RequestParam("file") MultipartFile file
            ) {
        String publicUrl = fileUploadService.uploadFile(file);

        FileUploadResponse response = FileUploadResponse.builder()
                .url(publicUrl)
                .build();

        return ApiResponse.success(response, "File uploaded successfully");
    }
}
