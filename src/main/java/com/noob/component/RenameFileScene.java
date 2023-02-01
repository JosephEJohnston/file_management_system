package com.noob.component;

import com.noob.component.config.RenameConfig;
import com.noob.model.bo.ManagedFile;
import com.noob.service.biz.FileBiz;
import jakarta.annotation.Resource;
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

import java.io.File;
import java.util.function.Consumer;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RenameFileScene {

    @Resource
    private FileBiz fileBiz;

    private Stage stage;

    private TextField originalFileName;

    private TextField newFileName;

    private Button cancelButton;

    private Button confirmButton;

    private final RenameConfig renameConfig;

    private Consumer<ManagedFile> callbackWhenExit;

    public RenameFileScene(
            RenameConfig renameConfig
    ) {
        this.renameConfig = renameConfig;
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
        originalFileName.setText(renameConfig.curFile().getName());
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

        cancelButton.setOnAction(event -> stage.close());
    }

    private void initConfirmButton() {
        confirmButton = new Button();
        confirmButton.setText("Confirm");
        confirmButton.setPrefWidth(65);
        confirmButton.setPrefHeight(24);
        confirmButton.setLayoutX(270);
        confirmButton.setLayoutY(150);

        confirmButton.setOnAction(event -> {
            renameFile();
            callbackWhenExit
                    .accept(renameConfig.curFile());
            stage.close();
        });
    }

    public void show() {
        stage.show();
    }

    public void renameFile() {
        File file = new File(renameConfig.curFile().getFullPath());

        if (!file.exists()) {
            return;
        }

        String newPath = file.getParent() + "\\" + newFileName.getText();
        boolean result = file.renameTo(new File(newPath));

        if (!result) {
            return;
        }

        renameConfig.curFile().setName(newFileName.getText());
        renameConfig.curFile().setFullPath(newPath);
        fileBiz.updateFile(renameConfig.curFile());
    }

    public void setCallbackWhenExit(Consumer<ManagedFile> callbackWhenExit) {
        this.callbackWhenExit = callbackWhenExit;
    }
}
