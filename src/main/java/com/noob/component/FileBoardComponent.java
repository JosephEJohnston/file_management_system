package com.noob.component;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FileBoardComponent {

    public FileBoardComponent() {

    }

    public Node makeFileBoard() {
        AnchorPane pane = new AnchorPane();

        pane.setPrefWidth(200);
        pane.setPrefHeight(200);
        pane.setLayoutX(200);
        pane.setLayoutY(0);

        Label curFileNameLabel = new Label();
        curFileNameLabel.setText("-");
        curFileNameLabel.setPrefWidth(200);
        curFileNameLabel.setPrefHeight(25);
        curFileNameLabel.setAlignment(Pos.CENTER);

        FlowPane tagFlowPane = new FlowPane();
        tagFlowPane.setStyle("-fx-background-color: yellow;");
        tagFlowPane.setPrefWidth(200);
        tagFlowPane.setPrefHeight(225);
        tagFlowPane.setLayoutX(0);
        tagFlowPane.setLayoutY(25);


        Label curFileStatusLabel = new Label();
        curFileStatusLabel.setText("-");
        curFileStatusLabel.setPrefWidth(85);
        curFileStatusLabel.setPrefHeight(20);
        curFileStatusLabel.setLayoutY(250);
        curFileStatusLabel.setAlignment(Pos.CENTER);

        pane.getChildren().addAll(curFileNameLabel, tagFlowPane, curFileStatusLabel);

        return pane;
    }
}
