package com.noob.component;

import com.noob.component.config.NormalConfig;
import com.noob.model.bo.*;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FileBoardComponent {

    private final NormalConfig config;

    private AnchorPane root;

    private Label curFileNameLabel;

    private FlowPane tagFlowPane;

    private Label curFileStatusLabel;

    private ManagedFile curFile;

    public FileBoardComponent(NormalConfig config) {
        this.config = config;
        initFileBoard();
    }

    public void initFileBoard() {
        root = new AnchorPane();
        root.setPrefWidth(200);
        root.setPrefHeight(200);
        root.setLayoutX(config.rootLayoutX());
        root.setLayoutY(config.rootLayoutY());

        initCurFileNameLabel();
        initTagFlowPane();
        initCurFileStatusLabel();

        root.getChildren().addAll(
                curFileNameLabel, tagFlowPane, curFileStatusLabel);
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

    public void showFile(SystemFile file) {
        if (file == null) {
            getCurFileNameLabel().setText("-");
            getCurFileStatusLabel().setText("-");

            return;
        }

        if (file instanceof SystemNotManagedFile f) {
            getCurFileNameLabel().setText(f.getFile().getName());
            getCurFileStatusLabel().setText("NO");
        } else if (file instanceof SystemNormalFile f) {
            showFile(f.getManagedFile());
        }
    }

    public void showFile(ManagedFile file) {
        curFile = file;

        removeTagList();
        if (curFile != null) {
            getCurFileNameLabel().setText(curFile.getName());
            getCurFileStatusLabel().setText("YES");
            showTagList();
        } else {
            getCurFileNameLabel().setText("-");
            getCurFileStatusLabel().setText("-");
        }
    }

    private void showTagList() {
        List<Button> tagList = curFile.getTagList().stream()
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

    public AnchorPane getRoot() {
        return root;
    }

    public Label getCurFileNameLabel() {
        return curFileNameLabel;
    }

    public Label getCurFileStatusLabel() {
        return curFileStatusLabel;
    }
}
