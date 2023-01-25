package com.noob.controller;

import com.noob.model.bo.SystemFile;
import com.noob.model.bo.SystemNormalFile;
import com.noob.model.bo.SystemNotManagedFile;
import com.noob.service.biz.FileBiz;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class MainSceneController implements Initializable {

    @Autowired
    private FileBiz fileBiz;

    @FXML
    private TextField pathTextField;

    @FXML
    private TreeView<SystemFile> fileTreeView;

    @FXML
    private Label curFileNameLabel;

    @FXML
    private FlowPane curFilePane;

    @FXML
    private Label curFileStatusLabel;

    private String directoryIconUrl;

    private SystemFile curSelectedFile;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Resource resource = new ClassPathResource("static/picture/folder_icon.png");
        try {
            directoryIconUrl = resource.getURL().toString();
        } catch (IOException ignore) {

        }
    }

    public void searchDirectory(ActionEvent event) {
        String directoryPath = pathTextField.getText();

        File directory = new File(directoryPath);
        if (!directory.isDirectory()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        fileTreeView.setRoot(makeRootTree(directory));
    }

    private TreeItem<SystemFile> makeRootTree(File directory) {
        File[] files = directory.listFiles();
        if (files == null) {
            return new TreeItem<>();
        }

        Pair<SystemNotManagedFile, List<SystemFile>> pair = fileBiz
                .renderSystemFileDirectory(directory);

        TreeItem<SystemFile> rootItem = new TreeItem<>(
                pair.getLeft(),
                new ImageView(new Image(directoryIconUrl))
        );

        pair.getRight().forEach(file -> rootItem
                .getChildren().add(new TreeItem<>(file)));

        return rootItem;
    }

    public void selectItem() {
        TreeItem<SystemFile> item = fileTreeView.getSelectionModel().getSelectedItem();
        curFilePane.getChildren().clear();

        if (item != null) {
            SystemFile systemFile = item.getValue();
            curSelectedFile = systemFile;

            curFileNameLabel.setText(systemFile.getFile().getName());

            if (systemFile instanceof SystemNormalFile) {
                curFileStatusLabel.setText("YES");
            } else {
                curFileStatusLabel.setText("NO");
            }


            /*List<Button> tagLabelList = tagList.stream()
                    .map(tag -> {
                        Button button = new Button(tag.getName());
                        button.setMinWidth(50);
                        button.setMinHeight(50);

                        return button;
                    }).toList();

            currentFilePane.getChildren().addAll(tagLabelList);*/
        }
    }

    public void manageFile() {
        if (curSelectedFile == null || curSelectedFile.getFile().isDirectory()) {
            return;
        }

        SystemFile newSystemFile = fileBiz.addManagedFile(curSelectedFile.getFile())
                .map(managedFile -> (SystemFile) SystemNormalFile
                        .of(curSelectedFile.getFile(), managedFile))
                .orElse(SystemNotManagedFile.of(curSelectedFile.getFile()));

        if (newSystemFile instanceof SystemNormalFile) {
            curFileStatusLabel.setText("YES");

            TreeItem<SystemFile> item = fileTreeView.getSelectionModel().getSelectedItem();
            item.setValue(newSystemFile);
        }
    }
}
