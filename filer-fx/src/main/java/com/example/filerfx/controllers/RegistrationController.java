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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

public class RegistrationController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField statusField; // Поле для ввода ответа

    @FXML
    private Label statusLabel;  // Метка для вопроса

    private final FileService fileService = new FileService();

    // Массив с вопросами и правильными ответами
    private String[] questions = {
            "Сколько будет 5 + 3?",  // Пример вопроса
            "Сколько будет 7 * 6?",  // Пример вопроса
            "Сколько будет 9 * 9?",  // Пример вопроса
            "Сколько будет 15 - 7?",  // Пример вопроса
            "Сколько будет 12 / 4?",  // Пример вопроса
            "Сколько будет 100 - 60?"  // Пример вопроса
    };

    private int[] correctAnswers = {8, 42, 81, 8, 3, 40};  // Правильные ответы

    // Индекс текущего вопроса
    private int currentQuestionIndex = 0;

    // Метод для задания случайного вопроса
    private void askQuestion() {
        // Выбираем случайный индекс вопроса
        Random random = new Random();
        currentQuestionIndex = random.nextInt(questions.length);

        // Задать вопрос на метке
        statusLabel.setText(questions[currentQuestionIndex]);
    }

    // Метод для проверки ответа
    private boolean checkAnswer(String answer) {
        try {
            int userAnswer = Integer.parseInt(answer);  // Преобразуем введенный ответ в число
            return userAnswer == correctAnswers[currentQuestionIndex];  // Сравниваем с правильным ответом
        } catch (NumberFormatException e) {
            return false;  // Если ответ не число, то считаем его неправильным
        }
    }

    @FXML
    public void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String userAnswer = statusField.getText();

        if (username.isEmpty() || password.isEmpty() || userAnswer.isEmpty()) {
            showAlert("Ошибка", "Все поля обязательны для заполнения.");
            return;
        }

        // Проверяем ответ
        boolean isVIP = checkAnswer(userAnswer);

        // Если ответ правильный, присваиваем статус "ВИП", если нет - "Простой"
        String status = isVIP ? "ВИП" : "Простой";

        try {
            User registeredUser = fileService.registerUser(username, password, status);
            showAlert("Успех", "Пользователь зарегистрирован с статусом: " + status);
            goBackToLogin();
        } catch (Exception e) {
            showAlert("Ошибка", "Не удалось зарегистрировать пользователя: " + e.getMessage());
        }
    }

    @FXML
    public void goBackToLogin() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("login.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.setTitle("Авторизация");
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось загрузить экран авторизации: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Вызываем этот метод для показа случайного вопроса
    @FXML
    public void initialize() {
        askQuestion();  // Инициализация с случайным вопросом
    }
}
