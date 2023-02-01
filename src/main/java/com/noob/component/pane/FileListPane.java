package com.noob.component.pane;

import com.noob.component.config.NormalConfig;
import com.noob.model.bo.*;
import com.noob.service.biz.FileBiz;
import com.noob.service.biz.TagBiz;
import jakarta.annotation.Resource;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FileListPane {

    @Resource
    private FileBiz fileBiz;

    private AnchorPane root;

    private Label curFileNameLabel;

    private FlowPane tagFlowPane;

    private Label curFileStatusLabel;

    private Button manageFileButton;

    private final NormalConfig config;

    private final ObjectProperty<SystemFile> curFile;

    private Consumer<SystemFile> callbackWhenManageFileSuccess;

    public FileListPane(NormalConfig config) {
        this.config = config;
        this.curFile = new SimpleObjectProperty<>(null);
        initFileBoard();
    }

    private void initFileBoard() {
        root = new AnchorPane();
        root.setPrefWidth(200);
        root.setPrefHeight(200);
        root.setLayoutX(config.rootLayoutX());
        root.setLayoutY(config.rootLayoutY());

        initCurFileNameLabel();
        initTagFlowPane();
        initCurFileStatusLabel();
        initManageFileButton();

        root.getChildren().addAll(curFileNameLabel, tagFlowPane,
                curFileStatusLabel, manageFileButton);
    }

    private void initCurFileNameLabel() {
        curFileNameLabel = new Label();
        curFileNameLabel.setText("-");
        curFileNameLabel.setFont(Font.font(15));
        curFileNameLabel.setPrefWidth(200);
        curFileNameLabel.setPrefHeight(25);
        curFileNameLabel.setAlignment(Pos.CENTER);
    }

    private void initTagFlowPane() {
        tagFlowPane = new FlowPane();
        tagFlowPane.setStyle("-fx-background-color: yellow;");
        tagFlowPane.setPrefWidth(200);
        tagFlowPane.setPrefHeight(225);
        tagFlowPane.setLayoutX(0);
        tagFlowPane.setLayoutY(25);
    }

    private void initCurFileStatusLabel() {
        curFileStatusLabel = new Label();
        curFileStatusLabel.setText("-");
        curFileStatusLabel.setFont(Font.font(15));
        curFileStatusLabel.setPrefWidth(85);
        curFileStatusLabel.setPrefHeight(20);
        curFileStatusLabel.setLayoutY(250);
        curFileStatusLabel.setAlignment(Pos.CENTER);
    }

    private void initManageFileButton() {
        manageFileButton = new Button();
        manageFileButton.setText("Manage This File");
        manageFileButton.setPrefWidth(115);
        manageFileButton.setPrefHeight(25);
        manageFileButton.setLayoutX(85);
        manageFileButton.setLayoutY(250);
        manageFileButton.setOnAction(event -> manageFile());
    }

    public void showFile(SystemFile file) {
        removeTagList();

        if (file == null) {
            curFileNameLabel.setText("-");
            curFileStatusLabel.setText("-");

            return;
        }

        this.curFile.set(file);
        curFileNameLabel.setText(file.getFile().getName());
        if (file instanceof SystemNotManagedFile) {
            curFileStatusLabel.setText("NO");
            manageFileButton.setDisable(false);
        } else if (file instanceof SystemNormalFile) {
            curFileStatusLabel.setText("YES");
            showTagList();
            manageFileButton.setDisable(true);
        }
    }

    private void showTagList() {
        if (!(this.curFile.get() instanceof SystemNormalFile)) {
            return;
        }

        List<Button> tagList = ((SystemNormalFile)curFile.get())
                .getManagedFile().getTagList().stream()
                .map(this::makeTagButton).toList();

        tagFlowPane.getChildren().addAll(tagList);
    }

    private Button makeTagButton(Tag tag) {
        Button button = new Button(tag.getName());
        button.setMinWidth(50);
        button.setMinHeight(30);

        return button;
    }

    private void removeTagList() {
        tagFlowPane.getChildren().clear();
    }

    public void manageFile() {
        SystemFile systemFile = curFile.get();
        if (systemFile == null || systemFile.getFile().isDirectory()) {
            return;
        }

        File file = systemFile.getFile();
        fileBiz.addManagedFile(file)
                .map(managedFile -> SystemNormalFile.of(file, managedFile))
                .ifPresent(f -> {
                    showFile(f);

                    if (callbackWhenManageFileSuccess != null) {
                        callbackWhenManageFileSuccess.accept(f);
                    }
                });
    }

    public void setCallbackWhenManageFileSuccess(Consumer<SystemFile> callbackWhenManageFileSuccess) {
        this.callbackWhenManageFileSuccess = callbackWhenManageFileSuccess;
    }

    public AnchorPane getRoot() {
        return root;
    }
}
