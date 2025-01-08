package com.example.filerfx.service;

import com.example.filerfx.models.DownloadRequest;
import com.example.filerfx.models.MyFile;
import com.example.filerfx.models.User;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class FileService {

    private final String BASE_URL = "http://localhost:8080/api/files";
    private final String USER_URL = "http://localhost:8080/api/user";
    private final RestTemplate restTemplate;

    public FileService() {
        this.restTemplate = new RestTemplate();
    }

    public List<MyFile> getAllFiles(String token) {
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

    public MyFile getFileById(Long id, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<MyFile> response = restTemplate.exchange(
                BASE_URL + "/" + id,
                HttpMethod.GET,
                request,
                MyFile.class
        );

        return response.getBody();
    }

    public void deleteFile(Long id, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        restTemplate.exchange(
                BASE_URL + "/" + id,
                HttpMethod.DELETE,
                request,
                Void.class
        );
    }

    public MyFile saveFile(MyFile file, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<MyFile> request = new HttpEntity<>(file, headers);
        ResponseEntity<MyFile> response = restTemplate.postForEntity(
                BASE_URL,
                request,
                MyFile.class
        );

        return response.getBody();
    }

    public String loginUser(String username, String password) {
        User user = new User(username, password, "");

        ResponseEntity<String> response = restTemplate.postForEntity(
                USER_URL + "/login",
                user,
                String.class
        );

        return response.getBody();
    }

    public User registerUser(String username, String password, String status) {
        User user = new User(username, password, status);

        ResponseEntity<User> response = restTemplate.postForEntity(
                USER_URL + "/register",
                user,
                User.class
        );

        return response.getBody();
    }

    public User getUserByName(String username, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<User> response = restTemplate.exchange(
                USER_URL + "/" + username,
                HttpMethod.GET,
                request,
                User.class
        );

        return response.getBody();
    }

    public void deleteUser(Long id, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        restTemplate.exchange(
                USER_URL + "/" + id,
                HttpMethod.DELETE,
                request,
                Void.class
        );
    }
}
