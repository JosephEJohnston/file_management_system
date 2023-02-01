package com.noob.controller;

import com.noob.MainIndex;
import com.noob.component.pane.FileBoardPane;
import com.noob.component.scene.RenameFileScene;
import com.noob.component.config.NormalConfig;
import com.noob.component.config.RenameConfig;
import com.noob.model.bo.ManagedFile;
import com.noob.model.bo.SystemNormalFile;
import com.noob.model.bo.Tag;
import com.noob.model.constants.Constants;
import com.noob.service.biz.FileTagBiz;
import com.noob.service.biz.SceneLoadBiz;
import jakarta.annotation.Resource;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

@Controller
public class TagSearchSceneController implements Initializable {

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private SceneLoadBiz sceneLoadBiz;

    @Resource
    private FileTagBiz fileTagBiz;

    @Resource
    private MainIndex mainIndex;

    @FXML
    private ListView<ManagedFile> searchFileListView;

    private List<Tag> searchTagList;

    private ContextMenu itemContextMenu;

    private FileBoardPane fileBoard;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchTagList = new ArrayList<>();
    }

    public void loadTagSearchStage(
            Tag initTag,
            Consumer<Void> refreshCallback
    ) throws IOException {
        FXMLLoader loader = sceneLoadBiz
                .makeFXMLLoader("fxml/TagSearchScene.fxml");

        AnchorPane root = loader.load();

        fileBoard = applicationContext.getBean(FileBoardPane.class,
                new NormalConfig(200, 0));
        root.getChildren().add(fileBoard.getRoot());

        addTagAndSearch(initTag);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(event ->
                refreshCallback.accept(null));
    }

    public void addTagAndSearch(Tag tag) {
        addTag(tag);

        searchFileByTag();
    }

    private void addTag(Tag tag) {
        searchTagList.add(tag);
    }

    private void searchFileByTag() {
        List<ManagedFile> managedFileList = fileTagBiz
                .searchFileByTagList(searchTagList);

        searchFileListView.getItems().addAll(managedFileList);
        searchFileListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> fileBoard
                        .showFile(SystemNormalFile.of(newValue)));
        searchFileListView.getSelectionModel().selectFirst();
    }

    public void selectPane(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            if(mouseEvent.getClickCount() == Constants.MOUSE_ONE_CLICK_COUNT) {
                selectPaneClickOnce();
            }
        }
    }

    private void selectPaneClickOnce() {
        if (itemContextMenu != null) {
            itemContextMenu.hide();
        }
    }

    public void selectItem(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            selectItemSecondaryMenu(mouseEvent);
        } else if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            if (mouseEvent.getClickCount() == Constants.MOUSE_DOUBLE_CLICK_COUNT){
                selectItemClickTwice();
            }
        }
    }

    private void selectItemClickTwice() {
        getCurrentSelectedFile()
                .map(ManagedFile::getFullPath)
                .ifPresent(path -> mainIndex
                        .getHostServices().showDocument(path));
    }

    private void selectItemSecondaryMenu(MouseEvent e) {
        MenuItem rename = new MenuItem("Rename");
        ContextMenu newMenu = new ContextMenu(rename);
        if (itemContextMenu != null && itemContextMenu.isShowing()) {
            return;
        }

        newMenu.setOnAction(event ->
                getCurrentSelectedFile()
                .ifPresent(f -> {
                    Consumer<ManagedFile> callback = (file) -> {
                        searchFileListView.refresh();
                        fileBoard.showFile(SystemNormalFile.of(file));
                    };

                    RenameFileScene renameFileScene = applicationContext
                            .getBean(RenameFileScene.class, new RenameConfig(f));
                    renameFileScene.setCallbackWhenExit(callback);

                    renameFileScene.show();
                }));

        itemContextMenu = newMenu;
        itemContextMenu.show(searchFileListView, e.getScreenX(), e.getScreenY());
    }

    private Optional<ManagedFile> getCurrentSelectedFile() {
        return Optional.ofNullable(searchFileListView.getSelectionModel().getSelectedItem());
    }
}
