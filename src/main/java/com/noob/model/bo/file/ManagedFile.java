package com.noob.model.bo.file;

import com.noob.model.bo.tag.Tag;
import lombok.Getter;
import lombok.ToString;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class ManagedFile {

    private boolean underManaged;

    private File file;

    private final List<Tag> tagList;

    private ManagedFile() {
        tagList = new ArrayList<>();
    }

    public static ManagedFile of(File file, List<Tag> tagList) {
        ManagedFile bo = new ManagedFile();

        bo.setFile(file);
        bo.addTag(tagList);

        return bo;
    }

    private void setFile(File file) {
        this.file = file;
    }

    public void addTag(List<Tag> tagList) {
        getTagList().addAll(tagList);
    }
}
