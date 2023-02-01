package com.noob.component.pane;

import com.noob.model.bo.Tag;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TagPane {

    private ListView<Tag> tagListView;

    private TextField addTagTextField;

    private Button relateToTagButton;

    public TagPane() {
    }


}
