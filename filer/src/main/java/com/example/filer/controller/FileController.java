package com.example.filer.controller;

import com.example.filer.dto.DownloadRequest;
import com.example.filer.jwt.JwtUtil;
import com.example.filer.models.File;
import com.example.filer.service.FileService;
import com.example.filer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/files")
public class FileController {
    @Autowired
    FileService fileService;
    @Autowired
    JwtUtil jwtUtil;

    @GetMapping
    public List<File> getAllFiles() {
        return fileService.getAllFiles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<File> getFileById(@PathVariable Long id) {
        Optional<File> file = fileService.getFileById(id);
        if (file.isPresent()) {
            return ResponseEntity.ok(file.get());
        } else {
            return ResponseEntity.notFound().build(); // 404
        }
    }

    @PostMapping
    public ResponseEntity<File> addFile(@RequestBody File file) {
        File savedFile = fileService.saveFile(file);
        return ResponseEntity.ok(savedFile);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        Optional<File> file = fileService.getFileById(id);
        if (file.isPresent()) {
            fileService.deleteFile(id);
            return ResponseEntity.noContent().build(); // 204
        } else {
            return ResponseEntity.notFound().build(); //  404
        }
    }

    @PostMapping("/download/{id}")
    public ResponseEntity<String> downloadFile(
            @PathVariable Long id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody DownloadRequest downloadRequest) {

        String jwtToken = token.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(jwtToken);
        String status = jwtUtil.extractStatus(jwtToken);

        Optional<File> fileOpt = fileService.getFileById(id);
        if (fileOpt.isPresent()) {
            try {
                if (status.equalsIgnoreCase("Простой")) {
                    fileService.saveFileWithDelay(fileOpt.get(), downloadRequest.getTargetPath(), 1024, 100);
                } else {
                    fileService.saveFileToTargetPath(fileOpt.get(), downloadRequest.getTargetPath());
                }
                return ResponseEntity.ok("Файл успешно скачан.");
            } catch (IOException e) {
                return ResponseEntity.noContent().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
