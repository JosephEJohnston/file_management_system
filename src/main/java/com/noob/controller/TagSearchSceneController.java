package com.noob.controller;

import com.noob.MainIndex;
import com.noob.model.bo.ManagedFile;
import com.noob.model.bo.Tag;
import com.noob.model.constants.Constants;
import com.noob.service.biz.FileTagBiz;
import com.noob.service.biz.SceneLoadBiz;
import jakarta.annotation.Resource;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Controller
public class TagSearchSceneController implements Initializable {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchTagList = new ArrayList<>();
    }

    public void loadTagSearchStage(Tag initTag) throws IOException {
        FXMLLoader loader = sceneLoadBiz
                .makeFXMLLoader("fxml/TagSearchScene.fxml");

        Parent root = loader.load();

        addTagAndSearch(initTag);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    public void addTagAndSearch(Tag tag) {
        searchTagList.add(tag);

        List<ManagedFile> managedFileList = fileTagBiz
                .searchFileByTagList(searchTagList);

        searchFileListView.getItems().addAll(managedFileList);
    }

    public void selectItem(MouseEvent mouseEvent) {

        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            selectItemSecondaryMenu(mouseEvent);
        } else if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            if(mouseEvent.getClickCount() == Constants.MOUSE_ONE_CLICK_COUNT) {
                selectItemClickOnce();
            } else if (mouseEvent.getClickCount() == Constants.MOUSE_DOUBLE_CLICK_COUNT){
                selectItemClickTwice();
            }
        }
    }

    private void selectItemClickOnce() {
        itemContextMenu.hide();
    }

    private void selectItemClickTwice() {
        getCurrentSelectedFile()
                .map(ManagedFile::getFullPath)
                .ifPresent(path -> mainIndex
                        .getHostServices().showDocument(path));
    }

    private void selectItemSecondaryMenu(MouseEvent e) {
        MenuItem rename = new MenuItem("rename");
        itemContextMenu = new ContextMenu(rename);

        itemContextMenu.show(searchFileListView, e.getScreenX(), e.getScreenY());
    }

    private Optional<ManagedFile> getCurrentSelectedFile() {
        return Optional.ofNullable(searchFileListView.getSelectionModel().getSelectedItem());
    }
}
