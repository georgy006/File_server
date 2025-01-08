package com.example.filerfx.controllers;

import com.example.filerfx.models.MyFile;
import com.example.filerfx.service.FileService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;


public class MainController {

    @FXML
    private TableView<MyFile> fileTable;

    @FXML
    private TableColumn<MyFile, Long> idColumn;

    @FXML
    private TableColumn<MyFile, String> nameColumn;

    @FXML
    private TableColumn<MyFile, String> typeColumn;

    @FXML
    private TableColumn<MyFile, String> pathColumn;

    @FXML
    private ProgressBar progressBar;

    private final FileService fileService = new FileService();
    private Stage primaryStage; // Главная сцена
    private String jwtToken; // JWT-токен пользователя

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    @FXML
    public void initialize() {
        // Связываем колонки с полями модели MyFile
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        pathColumn.setCellValueFactory(new PropertyValueFactory<>("path"));
        loadFiles();
    }

    @FXML
    public void loadFiles() {
        try {
            List<MyFile> files = fileService.getAllFiles(jwtToken);
            ObservableList<MyFile> observableFiles = FXCollections.observableArrayList(files);
            fileTable.setItems(observableFiles);
        } catch (Exception e) {
            showAlert("Ошибка", "Не удалось загрузить файлы: " + e.getMessage());
        }
    }

    @FXML
    public void downloadSelectedFile() {
        MyFile selectedFile = fileTable.getSelectionModel().getSelectedItem();

        if (selectedFile == null) {
            showAlert("Ошибка", "Выберите файл для скачивания!");
            return;
        }

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Выберите директорию для сохранения");

        File selectedDirectory = directoryChooser.showDialog(primaryStage);

        if (selectedDirectory != null) {
            String targetPath = selectedDirectory.getAbsolutePath();

            try {
                boolean success = fileService.downloadFile(selectedFile.getId(), targetPath, jwtToken);
                if (success) {
                    showAlert("Успех", "Файл успешно загружен в: " + targetPath);
                } else {
                    showAlert("Ошибка", "Не удалось загрузить файл!");
                }
            } catch (Exception e) {
                showAlert("Ошибка", "Произошла ошибка при загрузке файла: " + e.getMessage());
            }
        } else {
            showAlert("Отмена", "Директория не выбрана.");
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