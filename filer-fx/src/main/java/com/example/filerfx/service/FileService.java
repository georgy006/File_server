package com.example.filerfx.service;

import com.example.filerfx.models.DownloadRequest;
import com.example.filerfx.models.MyFile;
import com.example.filerfx.models.User;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;


public class FileService {

    private final String BASE_URL = "http://localhost:8080/api/files";
    private final String USER_URL = "http://localhost:8080/api/user";
    private final RestTemplate restTemplate;
    private static final Logger logger = Logger.getLogger(FileService.class.getName());

    public FileService() {
        this.restTemplate = new RestTemplate();
    }

    public List<MyFile> getAllFiles(String token) {
        logger.info("Токен для getAllFiles: " + token);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<MyFile[]> response = restTemplate.exchange(
                BASE_URL,
                HttpMethod.GET,
                request,
                MyFile[].class
        );

        return Arrays.asList(response.getBody());
    }

    public boolean downloadFile(Long id, String targetPath, String token) {
        logger.info("Токен для downloadFile: " + token);
        DownloadRequest downloadRequest = new DownloadRequest(targetPath);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<DownloadRequest> request = new HttpEntity<>(downloadRequest, headers);
        ResponseEntity<Void> response = restTemplate.postForEntity(
                BASE_URL + "/download/" + id,
                request,
                Void.class
        );

        return response.getStatusCode().is2xxSuccessful();
    }

    public String loginUser(String username, String password) {
        User user = new User(username, password, "");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<User> request = new HttpEntity<>(user, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                USER_URL + "/login",
                request,
                String.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody(); // Возвращаем JWT-токен
        } else {
            throw new RuntimeException("Ошибка при входе: " + response.getStatusCode());
        }
    }

    public User registerUser(String username, String password, String status) {
        User user = new User(username, password, status);

        ResponseEntity<User> response = restTemplate.postForEntity(
                USER_URL + "/register",
                user,
                User.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException("Ошибка при регистрации: " + response.getStatusCode());
        }
    }
}
