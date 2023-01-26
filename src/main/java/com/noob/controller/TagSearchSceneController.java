package com.noob.controller;

import com.noob.model.bo.ManagedFile;
import com.noob.model.bo.Tag;
import com.noob.service.biz.FileTagBiz;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class TagSearchSceneController implements Initializable {

    @Autowired
    private FileTagBiz fileTagBiz;

    @FXML
    private ListView<ManagedFile> searchFileListView;

    private List<Tag> searchTagList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchTagList = new ArrayList<>();
    }

    public void addTagAndSearch(Tag tag) {
        searchTagList.add(tag);

        List<ManagedFile> managedFileList = fileTagBiz
                .searchFileByTagList(searchTagList);

        searchFileListView.getItems().addAll(managedFileList);
    }
}
