package com.noob;

import com.noob.utils.H2Utils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@SpringBootApplication
@Component
public class MainIndex extends Application {

    private static ConfigurableApplicationContext applicationContext;

    private static String[] args;

    public static void main(String[] args) {
        MainIndex.args = args;
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        createTableIfNotExist();

        startStage(primaryStage);
    }

    private void createTableIfNotExist() {
        H2Utils.createTableIfNotExist();
    }

    private void startStage(Stage primaryStage) throws IOException {
        Resource resource = new ClassPathResource("fxml/IndexScene.fxml");
        FXMLLoader loader = new FXMLLoader(resource.getURL());
        loader.setControllerFactory(param -> applicationContext.getBean(param));

        Parent root = loader.load();
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnShowing(event -> {
            
        });
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
    }

    @Override
    public void init() {
        applicationContext = SpringApplication.run(MainIndex.class, args);
    }

    @Override
    public void stop() throws Exception {
        applicationContext.close();
    }
}
