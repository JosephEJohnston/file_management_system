package com.noob.controller;

import com.noob.model.bo.ManagedFile;
import com.noob.model.bo.Tag;
import com.noob.service.dao.FileService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class MainSceneController implements Initializable {

    @Autowired
    private FileService fileService;

    @FXML
    private TextField pathTextField;

    @FXML
    private TreeView<ManagedFile> fileTreeView;

    @FXML
    private Label currentFileNameLabel;

    @FXML
    private FlowPane currentFilePane;

    private String directory_icon_url;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Resource resource = new ClassPathResource("static/picture/folder_icon.png");
        try {
            directory_icon_url = resource.getURL().toString();
        } catch (IOException ignore) {

        }

        fileService.test().forEach(System.out::println);
    }

    public void changeDirectory(ActionEvent event) {
        String directoryPath = pathTextField.getText();

        File directory = new File(directoryPath);
        if (!directory.isDirectory()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        TreeItem<ManagedFile> rootItem = new TreeItem<>(ManagedFile.of(directory, Collections.emptyList()),
                new ImageView(new Image(directory_icon_url)));
        List<File> fileList = new ArrayList<>(List.of(files));

        fileList.forEach(file -> {
            TreeItem<ManagedFile> treeItem = new TreeItem<>(
                    ManagedFile.of(file, List.of(Tag.of("test_tag"))));
            rootItem.getChildren().add(treeItem);
        });

        fileTreeView.setRoot(rootItem);

    }

    public void selectItem() {
        TreeItem<ManagedFile> item = fileTreeView.getSelectionModel().getSelectedItem();
        currentFilePane.getChildren().clear();

        if (item != null) {
            ManagedFile managedFile = item.getValue();

            currentFileNameLabel.setText(managedFile.getFile().getName());

            List<Tag> tagList = managedFile.getTagList();

            List<Label> tagLabelList = tagList.stream()
                    .map(tag -> {
                        Label label = new Label(tag.getName());
                        label.setMinWidth(50);
                        label.setMinHeight(50);

                        return label;
                    }).toList();

            currentFilePane.getChildren().addAll(tagLabelList);
        }
    }
}
