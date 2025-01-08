package com.example.filerfx.models;

public class DownloadRequest {
    private String targetPath;

    public DownloadRequest() {}

    public DownloadRequest(String targetPath) {
        this.targetPath = targetPath;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }
}
