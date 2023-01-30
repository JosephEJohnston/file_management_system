package com.noob.component;

import com.noob.model.bo.ManagedFile;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RenameFileScene {

    private Stage stage;

    private TextField originalFileName;

    private TextField newFileName;

    private Button cancelButton;

    private Button confirmButton;

    private ManagedFile curFile;

    public RenameFileScene(ManagedFile curFile) {
        this.curFile = curFile;
        initStage();
    }

    private void initStage() {
        AnchorPane pane = new AnchorPane();
        pane.setPrefWidth(400);
        pane.setPrefHeight(200);

        Label original = new Label();
        original.setText("Original Name:");
        original.setFont(Font.font(15));
        original.setLayoutX(50);
        original.setLayoutY(40);

        originalFileName = new TextField();
        originalFileName.setFont(Font.font(15));
        originalFileName.setDisable(true);
        originalFileName.setLayoutX(180);
        originalFileName.setLayoutY(35);

        Label newName = new Label();
        newName.setText("New Name:");
        newName.setFont(Font.font(15));
        newName.setLayoutX(74);
        newName.setLayoutY(100);

        newFileName = new TextField();
        newFileName.setFont(Font.font(15));
        newFileName.setLayoutX(180);
        newFileName.setLayoutY(95);

        cancelButton = new Button();
        cancelButton.setText("Cancel");
        cancelButton.setPrefWidth(55);
        cancelButton.setPrefHeight(24);
        cancelButton.setLayoutX(70);
        cancelButton.setLayoutY(150);

        confirmButton = new Button();
        confirmButton.setText("Confirm");
        confirmButton.setPrefWidth(65);
        confirmButton.setPrefHeight(24);
        confirmButton.setLayoutX(270);
        confirmButton.setLayoutY(150);

        pane.getChildren().addAll(original, originalFileName, newName,
                newFileName, cancelButton, confirmButton);

        Scene scene = new Scene(pane);

        stage = new Stage();
        stage.setScene(scene);
    }

    public void show() {
        stage.show();
    }
}
