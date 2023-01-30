package com.noob.controller;

import com.noob.MainIndex;
import com.noob.component.FileBoardComponent;
import com.noob.model.bo.ManagedFile;
import com.noob.model.bo.Tag;
import com.noob.model.constants.Constants;
import com.noob.service.biz.FileTagBiz;
import com.noob.service.biz.SceneLoadBiz;
import jakarta.annotation.Resource;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MultipleSelectionModel;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchTagList = new ArrayList<>();
    }

    public void loadTagSearchStage(Tag initTag) throws IOException {
        FXMLLoader loader = sceneLoadBiz
                .makeFXMLLoader("fxml/TagSearchScene.fxml");

        AnchorPane root = loader.load();

        FileBoardComponent component = applicationContext.getBean(FileBoardComponent.class);
        root.getChildren().add(component.makeFileBoard());

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

        itemContextMenu = newMenu;
        itemContextMenu.show(searchFileListView, e.getScreenX(), e.getScreenY());
    }

    private Optional<ManagedFile> getCurrentSelectedFile() {
        return Optional.ofNullable(searchFileListView.getSelectionModel().getSelectedItem());
    }
}
