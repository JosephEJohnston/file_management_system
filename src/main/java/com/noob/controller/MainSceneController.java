package com.noob.controller;

import com.noob.MainIndex;
import com.noob.component.pane.FileListPane;
import com.noob.component.config.NormalConfig;
import com.noob.component.pane.TagListPane;
import com.noob.model.bo.*;
import com.noob.model.constants.Constants;
import com.noob.model.event.CommunicationEvent;
import com.noob.service.biz.FileBiz;
import jakarta.annotation.Resource;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.net.URL;
import java.util.*;

@Controller
public class MainSceneController implements Initializable {

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private FileBiz fileBiz;

    @Resource
    private MainIndex mainIndex;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField pathTextField;

    @FXML
    private ListView<SystemFile> fileListView;

    private FileListPane fileListPane;

    private TagListPane tagListPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initFileListPane();
        initTagListPane();

        rootPane.getChildren().addAll(fileListPane.getRoot(), tagListPane.getRoot());
        rootPane.addEventHandler(CommunicationEvent.MANAGE_SUCCESS, event -> {
            int selectedIndex = fileListView.getSelectionModel().getSelectedIndex();
            fileListView.getItems().set(selectedIndex, event.getFile());
        });
    }

    private void initFileListPane() {
        fileListPane = applicationContext.getBean(FileListPane.class,
                new NormalConfig(400, 30));
    }

    private void initTagListPane() {
        tagListPane = applicationContext.getBean(TagListPane.class,
                new NormalConfig(600, 0));

        tagListPane.searchTagList();
        tagListPane.setCallbackWhenCloseTagSearchScene(
                () -> fileListView.refresh());
        tagListPane.setCallbackWhenRelateGetFile(
                () -> getCurrentSelectedFile().orElse(null));
        tagListPane.setCallbackWhenRelateFinish(
                (f) -> fileListPane.showFile(SystemNormalFile.of(f)));
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

        fileListView.getItems().setAll(makeSystemFileList(directory));
        fileListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> fileListPane.showFile(newValue));
    }

    private List<SystemFile> makeSystemFileList(File directory) {
        File[] files = directory.listFiles();
        if (files == null) {
            return new ArrayList<>();
        }

        Pair<SystemNotManagedFile, List<SystemFile>> pair = fileBiz
                .renderSystemFileDirectory(directory);

        return pair.getRight();
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

    private Optional<SystemFile> getCurrentSelectedFile() {
        return Optional.ofNullable(fileListView.getSelectionModel().getSelectedItem());
    }
}
