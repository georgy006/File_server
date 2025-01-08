package com.example.filerfx.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MyFile {
    private Long id;

    @JsonProperty("fileName")
    private String name;

    @JsonProperty("fileType")
    private String type;

    @JsonProperty("filePath")
    private String path;

    // Конструктор без аргументов
    public MyFile() {}

    // Конструктор с аргументами (необязательно)
    public MyFile(Long id, String name, String type, String path) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.path = path;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
