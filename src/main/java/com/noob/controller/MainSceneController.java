package com.noob.controller;

import com.noob.MainIndex;
import com.noob.model.bo.SystemFile;
import com.noob.model.bo.SystemNormalFile;
import com.noob.model.bo.SystemNotManagedFile;
import com.noob.model.bo.Tag;
import com.noob.model.constants.Contants;
import com.noob.service.biz.FileBiz;
import com.noob.service.biz.TagBiz;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
import java.util.Optional;
import java.util.ResourceBundle;

@Controller
public class MainSceneController implements Initializable {

    @Autowired
    private FileBiz fileBiz;

    @Autowired
    private TagBiz tagBiz;

    @Autowired
    private MainIndex mainIndex;

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

    @FXML
    private TextField addTagTextField;

    @FXML
    private ListView<Tag> tagListView;

    private String directoryIconUrl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initIconUrl();
        initTagListView();
        initTreeView();
    }

    private void initIconUrl() {
        Resource resource = new ClassPathResource("static/picture/folder_icon.png");
        try {
            directoryIconUrl = resource.getURL().toString();
        } catch (IOException ignore) {

        }
    }

    private void initTagListView() {
        List<Tag> tagList = tagBiz.getAllTag();

        tagListView.getItems().addAll(tagList);
    }

    private void initTreeView() {

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

    public void upDirectory(ActionEvent event) {
        Optional<String> parentPathOpt = Optional
                .ofNullable(pathTextField.getText())
                .map(File::new)
                .map(File::getParent);

        if (parentPathOpt.isPresent()) {
            pathTextField.setText(parentPathOpt.get());
            searchDirectory(null);
        }
    }

    public void selectItem(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == Contants.MOUSE_DOUBLE_CLICK_COUNT) {
            selectItemClickTwice(mouseEvent);
        } else {
            selectItemClickOnce(mouseEvent);
        }
    }

    private void selectItemClickTwice(MouseEvent mouseEvent) {
        getCurrentItem()
                .map(TreeItem::getValue)
                .map(SystemFile::getFile)
                .ifPresent(file -> mainIndex
                        .getHostServices()
                        .showDocument(file.getAbsolutePath()));
    }

    private void selectItemClickOnce(MouseEvent mouseEvent) {
        Optional<TreeItem<SystemFile>> item = getCurrentItem();
        curFilePane.getChildren().clear();

        System.out.println(item);
        if (item.isEmpty()) {
            return;
        }

        SystemFile systemFile = item.get().getValue();

        curFileNameLabel.setText(systemFile.getFile().getName());

        if (systemFile instanceof SystemNormalFile normalFile) {
            curFileStatusLabel.setText("YES");

            List<Tag> tagList = normalFile.getManagedFile().getTagList();
            List<Button> tagLabelList = tagList.stream()
                    .map(tag -> {
                        Button button = new Button(tag.getName());
                        button.setMinWidth(50);
                        button.setMinHeight(30);

                        return button;
                    }).toList();

            curFilePane.getChildren().addAll(tagLabelList);
        } else {
            curFileStatusLabel.setText("NO");
        }
    }

    public void manageFile() {
        Optional<TreeItem<SystemFile>> currentItemOpt = getCurrentItem();
        Optional<SystemFile> systemFileOpt = currentItemOpt
                .map(TreeItem::getValue);
        if (systemFileOpt.isEmpty() || systemFileOpt.get().getFile().isDirectory()) {
            return;
        }

        SystemFile curSelectedFile = systemFileOpt.get();
        SystemFile newSystemFile = fileBiz.addManagedFile(curSelectedFile.getFile())
                .map(managedFile -> (SystemFile) SystemNormalFile
                        .of(curSelectedFile.getFile(), managedFile))
                .orElse(SystemNotManagedFile.of(curSelectedFile.getFile()));

        if (newSystemFile instanceof SystemNormalFile) {
            curFileStatusLabel.setText("YES");

            TreeItem<SystemFile> item = currentItemOpt.get();
            item.setValue(newSystemFile);
        }
    }

    public void addTag(ActionEvent event) {
        String tagName = addTagTextField.getText();

        Optional<Tag> optionalTag = tagBiz.addTag(tagName);

        optionalTag.ifPresent(tag ->
                tagListView.getItems().add(tag));
    }

    public void curFileRelateToTag(ActionEvent event) {
        System.out.println(getCurrentItem());
    }

    private Optional<TreeItem<SystemFile>> getCurrentItem() {
        return Optional.ofNullable(fileTreeView.getSelectionModel().getSelectedItem());
    }
}
