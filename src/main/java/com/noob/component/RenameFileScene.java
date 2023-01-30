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

    private final ManagedFile curFile;

    public RenameFileScene(ManagedFile curFile) {
        this.curFile = curFile;
        initStage();
    }

    private void initStage() {
        AnchorPane pane = new AnchorPane();
        pane.setPrefWidth(400);
        pane.setPrefHeight(200);

        Label original = makeOriginalLabel();
        initOriginalFileNameTextField();
        Label newName = makeNewNameLabel();
        initNewFileNameTextField();
        initCancelButton();
        initConfirmButton();

        pane.getChildren().addAll(original, originalFileName, newName,
                newFileName, cancelButton, confirmButton);

        Scene scene = new Scene(pane);

        stage = new Stage();
        stage.setScene(scene);
    }

    private Label makeOriginalLabel() {
        Label original = new Label();
        original.setText("Original Name:");
        original.setFont(Font.font(15));
        original.setLayoutX(50);
        original.setLayoutY(40);

        return original;
    }

    private void initOriginalFileNameTextField() {
        originalFileName = new TextField();
        originalFileName.setText(curFile.getName());
        originalFileName.setFont(Font.font(15));
        originalFileName.setDisable(true);
        originalFileName.setLayoutX(180);
        originalFileName.setLayoutY(35);
    }

    private Label makeNewNameLabel() {
        Label newName = new Label();
        newName.setText("New Name:");
        newName.setFont(Font.font(15));
        newName.setLayoutX(74);
        newName.setLayoutY(100);

        return newName;
    }

    private void initNewFileNameTextField() {
        newFileName = new TextField();
        newFileName.setFont(Font.font(15));
        newFileName.setLayoutX(180);
        newFileName.setLayoutY(95);
    }

    private void initCancelButton() {
        cancelButton = new Button();
        cancelButton.setText("Cancel");
        cancelButton.setPrefWidth(55);
        cancelButton.setPrefHeight(24);
        cancelButton.setLayoutX(70);
        cancelButton.setLayoutY(150);
    }

    private void initConfirmButton() {
        confirmButton = new Button();
        confirmButton.setText("Confirm");
        confirmButton.setPrefWidth(65);
        confirmButton.setPrefHeight(24);
        confirmButton.setLayoutX(270);
        confirmButton.setLayoutY(150);
    }

    public void show() {
        stage.show();
    }
}
