package com.noob.model.bo;

import com.noob.model.po.FilePO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ManagedFile {
    private Long id;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final StringProperty name;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final StringProperty fullPath;

    private Integer type;

    private List<Tag> tagList;

    private ManagedFile() {
        this.tagList = new ArrayList<>();
        name = new SimpleStringProperty(this, "name", "");
        fullPath = new SimpleStringProperty(this, "fullPath", "");
    }

    public static ManagedFile of(FilePO po) {
        ManagedFile managedFile = new ManagedFile();

        BeanUtils.copyProperties(po, managedFile);
        managedFile.setName(po.getName());
        managedFile.setFullPath(po.getFullPath());

        return managedFile;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getFullPath() {
        return fullPath.get();
    }

    public void setFullPath(String fullPath) {
        this.fullPath.set(fullPath);
    }

    @Override
    public String toString() {
        return name.get();
    }
}
