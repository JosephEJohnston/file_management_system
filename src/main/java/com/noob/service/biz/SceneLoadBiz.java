package com.noob.service.biz;

import javafx.fxml.FXMLLoader;

import java.io.IOException;

public interface SceneLoadBiz {
    FXMLLoader makeFXMLLoader(String sourceClassPath) throws IOException;
}
