module com.example.filerfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    // Для работы с REST API через Spring
    requires spring.web;
    requires com.fasterxml.jackson.databind;

    // Открываем доступ для JavaFX FXML и работы с контроллерами
    opens com.example.filerfx to javafx.fxml;
    opens com.example.filerfx.controllers to javafx.fxml;

    // Открываем модели для Jackson и JavaFX
    opens com.example.filerfx.models to com.fasterxml.jackson.databind, javafx.base;

    // Экспортируем базовый пакет
    exports com.example.filerfx;
}
