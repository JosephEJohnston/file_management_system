package com.noob.component.pane;

import com.noob.component.config.NormalConfig;
import com.noob.model.bo.Tag;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TagPane {

    private AnchorPane root;

    private ListView<Tag> tagListView;

    private Label addTagLabel;

    private TextField addTagTextField;

    private Button relateToTagButton;

    private final NormalConfig config;

    public TagPane(NormalConfig config) {
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
    }

    private void initRelateToTagButton() {
        relateToTagButton = new Button();
        relateToTagButton.setPrefWidth(115);
        relateToTagButton.setPrefHeight(25);
        relateToTagButton.setLayoutX(0);
        relateToTagButton.setLayoutY(385);

    }

    public AnchorPane getRoot() {
        return root;
    }
}
