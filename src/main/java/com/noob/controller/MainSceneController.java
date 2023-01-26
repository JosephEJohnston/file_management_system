package com.noob.controller;

import com.noob.MainIndex;
import com.noob.model.bo.*;
import com.noob.model.constants.Contants;
import com.noob.service.biz.FileBiz;
import com.noob.service.biz.FileTagBiz;
import com.noob.service.biz.TagBiz;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
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
    private FileBiz fileBiz;

    @Autowired
    private TagBiz tagBiz;

    @Autowired
    private FileTagBiz fileTagBiz;

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

    public void upDirectory() {
        Optional<String> parentPathOpt = Optional
                .ofNullable(pathTextField.getText())
                .map(File::new)
                .map(File::getParent);

        if (parentPathOpt.isPresent()) {
            pathTextField.setText(parentPathOpt.get());
            searchDirectory();
        }
    }

    public void selectItem(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == Contants.MOUSE_DOUBLE_CLICK_COUNT) {
            selectItemClickTwice();
        } else {
            selectItemClickOnce();
        }
    }

    private void selectItemClickTwice() {
        getCurrentSelectedFile()
                .map(TreeItem::getValue)
                .map(SystemFile::getFile)
                .ifPresent(file -> mainIndex
                        .getHostServices()
                        .showDocument(file.getAbsolutePath()));
    }

    private void selectItemClickOnce() {
        Optional<TreeItem<SystemFile>> item = getCurrentSelectedFile();
        curFilePane.getChildren().clear();

        if (item.isEmpty()) {
            return;
        }

        SystemFile systemFile = item.get().getValue();

        curFileNameLabel.setText(systemFile.getFile().getName());

        if (systemFile instanceof SystemNormalFile normalFile) {
            curFileStatusLabel.setText("YES");

            List<Tag> tagList = normalFile.getManagedFile().getTagList();
            List<Button> tagLabelList = tagList.stream()
                    .map(this::makeTagButton).toList();

            curFilePane.getChildren().addAll(tagLabelList);
        } else {
            curFileStatusLabel.setText("NO");
        }
    }

    private Button makeTagButton(Tag tag) {
        Button button = new Button(tag.getName());
        button.setMinWidth(50);
        button.setMinHeight(30);

        return button;
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
            curFileStatusLabel.setText("YES");

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
        Optional<SystemFile> optFile = getCurrentSelectedFile()
                .map(TreeItem::getValue)
                .filter(f -> f instanceof SystemNormalFile);

        Optional<Tag> optTag = getCurrentSelectedTag();

        if (optFile.isEmpty() || optTag.isEmpty()) {
            return;
        }

        SystemNormalFile systemNormalFile = (SystemNormalFile) optFile.get();
        Tag tag = optTag.get();

        ManagedFile managedFile = systemNormalFile.getManagedFile();
        Set<String> fileTagNameSet = managedFile.getTagList()
                .stream().map(Tag::getName).collect(Collectors.toSet());

        if (fileTagNameSet.contains(tag.getName())) {
            return;
        }

        boolean result = fileTagBiz.relateFileToTag(managedFile, tag);
        if (!result) {
            return;
        }

        managedFile.getTagList().add(tag);
        selectItemClickOnce();
    }

    public void selectTag(MouseEvent mouseEvent) throws IOException {
        if(mouseEvent.getClickCount() == Contants.MOUSE_DOUBLE_CLICK_COUNT) {
            selectTagClickTwice();
        }
    }

    public void selectTagClickTwice() throws IOException {
        Resource resource = new ClassPathResource("fxml/TagSearchScene.fxml");
        FXMLLoader loader = new FXMLLoader(resource.getURL());
        loader.setControllerFactory(param -> applicationContext.getBean(param));

        Parent root = loader.load();
        TagSearchSceneController controller = loader.getController();

        getCurrentSelectedTag()
                .ifPresent(controller::addTagAndSearch);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    private Optional<TreeItem<SystemFile>> getCurrentSelectedFile() {
        return Optional.ofNullable(fileTreeView.getSelectionModel().getSelectedItem());
    }

    private Optional<Tag> getCurrentSelectedTag() {
        return Optional.ofNullable(tagListView.getSelectionModel().getSelectedItem());
    }
}
