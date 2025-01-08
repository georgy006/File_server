package com.example.filerfx.controllers;

import com.example.filerfx.Application;
import com.example.filerfx.models.User;
import com.example.filerfx.service.FileService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private Label currentUserLabel;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private final FileService fileService = new FileService();
    private User currentUser; // Текущий пользователь
    private String jwtToken; // Токен пользователя для дальнейших запросов

    @FXML
    public void initialize() {
        // Обновляем метку с текущим пользователем
        updateCurrentUserLabel();
    }

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Ошибка", "Введите логин и пароль.");
            return;
        }

        System.out.println("Попытка входа:");
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);

        try {
            jwtToken = fileService.loginUser(username, password);
            System.out.println("Получен JWT-токен: " + jwtToken);

            if (jwtToken == null || jwtToken.isEmpty()) {
                showAlert("Ошибка", "Неверные учетные данные.");
                return;
            }

            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load());

            MainController mainController = fxmlLoader.getController();
            mainController.initializeWithToken(jwtToken); // Передаём токен и вызываем loadFiles

            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("FileServer");

        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось загрузить главное окно: " + e.getMessage());
        } catch (Exception e) {
            showAlert("Ошибка", "Произошла ошибка при входе: " + e.getMessage());
            e.printStackTrace();
        }
    }




    @FXML
    public void goToRegister() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("register.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.setResizable(false);
            stage.setTitle("Регистрация");
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось загрузить экран регистрации: " + e.getMessage());
        }
    }

    private void updateCurrentUserLabel() {
        if (currentUser != null) {
            currentUserLabel.setText("Вы вошли как: " + currentUser.getUsername());
        } else {
            currentUserLabel.setText("Вы еще не вошли");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
