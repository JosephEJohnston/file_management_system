package com.noob.controller;

import com.noob.MainIndex;
import com.noob.component.FileBoardPane;
import com.noob.component.config.NormalConfig;
import com.noob.model.bo.*;
import com.noob.model.constants.Constants;
import com.noob.service.biz.FileBiz;
import com.noob.service.biz.FileTagBiz;
import com.noob.service.biz.TagBiz;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class MainSceneController implements Initializable {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TagSearchSceneController tagSearchSceneController;

    @Autowired
    private FileBiz fileBiz;

    @Autowired
    private TagBiz tagBiz;

    @Autowired
    private FileTagBiz fileTagBiz;

    @Autowired
    private MainIndex mainIndex;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField pathTextField;

    @FXML
    private TreeView<SystemFile> fileTreeView;

    @FXML
    private TextField addTagTextField;

    @FXML
    private ListView<Tag> tagListView;

    private FileBoardPane fileBoard;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTagListView();
        fileBoard = applicationContext.getBean(FileBoardPane.class,
                new NormalConfig(400, 30));

        rootPane.getChildren().add(fileBoard.getRoot());
    }

    private void initTagListView() {
        List<Tag> tagList = tagBiz.getAllTag();

        tagListView.getItems().addAll(tagList);
    }

    public void searchDirectory(String path) {
        pathTextField.setText(path);
        searchDirectory();
    }

    public void searchDirectory() {
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
        fileTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            SystemFile file = Optional.ofNullable(newValue)
                    .map(TreeItem::getValue)
                    .orElse(null);

            fileBoard.showFile(file);
        });
    }

    private TreeItem<SystemFile> makeRootTree(File directory) {
        File[] files = directory.listFiles();
        if (files == null) {
            return new TreeItem<>();
        }

        Pair<SystemNotManagedFile, List<SystemFile>> pair = fileBiz
                .renderSystemFileDirectory(directory);

        TreeItem<SystemFile> rootItem = new TreeItem<>(pair.getLeft());

        pair.getRight().forEach(file -> rootItem
                .getChildren().add(new TreeItem<>(file)));

        return rootItem;
    }

    public void upDirectory() {
        Optional.ofNullable(pathTextField.getText())
                .map(File::new)
                .map(File::getParent)
                .ifPresent(this::searchDirectory);
    }

    public void selectItem(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == Constants.MOUSE_DOUBLE_CLICK_COUNT) {
            selectItemClickTwice();
        }
    }

    private void selectItemClickTwice() {
        getCurrentSelectedFile()
                .map(TreeItem::getValue)
                .map(SystemFile::getFile)
                .ifPresent(file -> {
                    if (file.isFile()) {
                        mainIndex.getHostServices()
                                .showDocument(file.getAbsolutePath());
                    } else {
                        searchDirectory(file.getAbsolutePath());
                    }
                });
    }

    public void manageFile() {
        Optional<TreeItem<SystemFile>> currentItemOpt = getCurrentSelectedFile();
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
            fileBoard.showFile(newSystemFile);

            TreeItem<SystemFile> item = currentItemOpt.get();
            item.setValue(newSystemFile);
        }
    }

    public void addTag() {
        String tagName = addTagTextField.getText();

        Optional<Tag> optionalTag = tagBiz.addTag(tagName);

        optionalTag.ifPresent(tag ->
                tagListView.getItems().add(tag));
    }

    public void curFileRelateToTag() {
        Optional<SystemNormalFile> optFile = getCurrentSelectedFile()
                .map(TreeItem::getValue)
                .filter(f -> f instanceof SystemNormalFile)
                .map(f -> (SystemNormalFile) f);

        Optional<Tag> optTag = getCurrentSelectedTag();

        if (optFile.isEmpty() || optTag.isEmpty()) {
            return;
        }

        ManagedFile managedFile = optFile.get().getManagedFile();
        Set<String> fileTagNameSet = managedFile.getTagList()
                .stream().map(Tag::getName).collect(Collectors.toSet());

        Tag tag = optTag.get();
        if (fileTagNameSet.contains(tag.getName())) {
            return;
        }

        boolean result = fileTagBiz.relateFileToTag(managedFile, tag);
        if (!result) {
            return;
        }

        managedFile.getTagList().add(tag);
        fileBoard.showFile(SystemNormalFile.of(managedFile));
    }

    public void selectTag(MouseEvent mouseEvent) throws IOException {
        if(mouseEvent.getClickCount() == Constants.MOUSE_DOUBLE_CLICK_COUNT) {
            selectTagClickTwice();
        }
    }

    public void selectTagClickTwice() throws IOException {
        Optional<Tag> tagOpt = getCurrentSelectedTag();
        if (tagOpt.isPresent()) {
            tagSearchSceneController.loadTagSearchStage(tagOpt.get());
        }
    }

    private Optional<TreeItem<SystemFile>> getCurrentSelectedFile() {
        return Optional.ofNullable(fileTreeView.getSelectionModel().getSelectedItem());
    }

    private Optional<Tag> getCurrentSelectedTag() {
        return Optional.ofNullable(tagListView.getSelectionModel().getSelectedItem());
    }
}
