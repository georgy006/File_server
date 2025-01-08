package com.example.filer.service.impl;

import com.example.filer.models.File;
import com.example.filer.repository.FileRepository;
import com.example.filer.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    FileRepository fileRepository;

    // Добавление файла
    public File saveFile(File file) {
        return fileRepository.save(file);
    }

    // Получение всех файлов
    public List<File> getAllFiles() {
        return fileRepository.findAll();
    }

    // Поиск файла по ID
    public Optional<File> getFileById(Long id) {
        return fileRepository.findById(id);
    }

    // Удаление файла по ID
    public void deleteFile(Long id) {
        fileRepository.deleteById(id);
    }

    public void saveFileToTargetPath(File file, String targetPath) throws IOException {
        Path sourceFilePath = Paths.get(file.getFilePath());
        if (Files.exists(sourceFilePath)) {
            // Добавляем имя файла к целевому пути
            Path targetDir = Paths.get(targetPath);
            Path targetFilePath = targetDir.resolve(sourceFilePath.getFileName());

            // Создаем директорию, если она не существует
            Files.createDirectories(targetDir);

            // Копируем файл
            Files.copy(sourceFilePath, targetFilePath, StandardCopyOption.REPLACE_EXISTING);
        } else {
            throw new IOException("Source file does not exist: " + sourceFilePath.toString());
        }
    }

    public void saveFileWithDelay(File file, String targetPath, int chunkSize, long delayMillis) throws IOException {
        Path sourceFilePath = Paths.get(file.getFilePath());
        if (Files.exists(sourceFilePath)) {
            // Добавляем имя файла к целевому пути
            Path targetDir = Paths.get(targetPath);
            Path targetFilePath = targetDir.resolve(sourceFilePath.getFileName());

            // Создаем директорию, если она не существует
            Files.createDirectories(targetDir);

            // Копируем файл с задержкой
            try (var inputStream = Files.newInputStream(sourceFilePath);
                 var outputStream = Files.newOutputStream(targetFilePath)) {
                byte[] buffer = new byte[chunkSize];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    outputStream.flush();
                    try {
                        Thread.sleep(delayMillis); // Добавляем задержку
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Восстанавливаем статус потока
                        throw new IOException("Thread was interrupted during file copy", e);
                    }
                }
            }
        } else {
            throw new IOException("Source file does not exist: " + sourceFilePath.toString());
        }
    }
}
