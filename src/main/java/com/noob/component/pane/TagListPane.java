package com.noob.component.pane;

import com.noob.component.config.NormalConfig;
import com.noob.controller.TagSearchSceneController;
import com.noob.model.bo.ManagedFile;
import com.noob.model.bo.SystemFile;
import com.noob.model.bo.SystemNormalFile;
import com.noob.model.bo.Tag;
import com.noob.model.constants.Constants;
import com.noob.model.event.CommunicationEvent;
import com.noob.service.biz.FileTagBiz;
import com.noob.service.biz.TagBiz;
import jakarta.annotation.Resource;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TagListPane {

    @Resource
    private TagSearchSceneController tagSearchSceneController;

    @Resource
    private TagBiz tagBiz;

    @Resource
    private FileTagBiz fileTagBiz;

    private AnchorPane root;

    private ListView<Tag> tagListView;

    private Label addTagLabel;

    private TextField addTagTextField;

    private Button relateToTagButton;

    private final NormalConfig config;

    private Runnable callbackWhenCloseTagSearchScene;

    private Supplier<SystemFile> callbackWhenRelate;

    public TagListPane(NormalConfig config) {
        this.config = config;
        initTagPane();
    }

    private void initTagPane() {
        root = new AnchorPane();
        root.setPrefWidth(200);
        root.setPrefHeight(370);
        root.setLayoutX(config.rootLayoutX());
        root.setLayoutY(config.rootLayoutY());

        initTagListView();
        initAddTagLabel();
        initAddTagTextField();
        initRelateToTagButton();

        root.getChildren().addAll(tagListView, addTagLabel,
                addTagTextField, relateToTagButton);
    }

    private void initTagListView() {
        tagListView = new ListView<>();
        tagListView.setPrefWidth(200);
        tagListView.setPrefHeight(320);
        tagListView.setLayoutX(0);
        tagListView.setLayoutY(40);

        tagListView.setOnMouseClicked(this::selectTag);
    }

    public void searchTagList() {
        // spring bean 在构造器阶段还未注入
        tagListView.getItems().addAll(tagBiz.getAllTag());
    }

    private void initAddTagLabel() {
        addTagLabel = new Label();
        addTagLabel.setText("Add Tag:");
        addTagLabel.setPrefWidth(75);
        addTagLabel.setPrefHeight(25);
        addTagLabel.setLayoutX(0);
        addTagLabel.setLayoutY(360);
    }

    private void initAddTagTextField() {
        addTagTextField = new TextField();
        addTagTextField.setPrefWidth(120);
        addTagTextField.setPrefHeight(25);
        addTagTextField.setLayoutX(80);
        addTagTextField.setLayoutY(360);

        addTagTextField.setOnAction(event -> addTag());
    }

    private void initRelateToTagButton() {
        relateToTagButton = new Button();
        relateToTagButton.setText("Relate To Tag");
        relateToTagButton.setPrefWidth(115);
        relateToTagButton.setPrefHeight(25);
        relateToTagButton.setLayoutX(0);
        relateToTagButton.setLayoutY(385);

        relateToTagButton.setOnAction(event -> makeFileRelateToTag());
    }

    private void addTag() {
        tagBiz.addTag(addTagTextField.getText())
                .ifPresent(tag -> tagListView.getItems().add(tag));
    }

    public void makeFileRelateToTag() {
        Optional<SystemNormalFile> optFile = Optional
                .ofNullable(callbackWhenRelate)
                .map(Supplier::get)
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
        root.fireEvent(new CommunicationEvent(
                CommunicationEvent.RELATE_FINISH, SystemNormalFile.of(managedFile)));
    }

    private void selectTag(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == Constants.MOUSE_DOUBLE_CLICK_COUNT) {
            selectTagClickTwice();
        }
    }

    private void selectTagClickTwice() {
        getCurrentSelectedTag().ifPresent(tag -> tagSearchSceneController
                .loadTagSearchStage(tag, callbackWhenCloseTagSearchScene));
    }

    private Optional<Tag> getCurrentSelectedTag() {
        return Optional.ofNullable(tagListView.getSelectionModel().getSelectedItem());
    }

    public void setCallbackWhenCloseTagSearchScene(Runnable callbackWhenCloseTagSearchScene) {
        this.callbackWhenCloseTagSearchScene = callbackWhenCloseTagSearchScene;
    }

    public void setCallbackWhenRelateGetFile(Supplier<SystemFile> callbackWhenRelate) {
        this.callbackWhenRelate = callbackWhenRelate;
    }

    public AnchorPane getRoot() {
        return root;
    }
}
