package com.example.filer.service;

import com.example.filer.models.File;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface FileService {
     File saveFile(File file);
     List<File> getAllFiles();
     Optional<File> getFileById(Long id);
     void deleteFile(Long id);
     void saveFileToTargetPath(File file, String targetPath) throws IOException;
     void saveFileWithDelay(File file, String targetPath, int chunkSize, long delayMillis) throws IOException;

    }
