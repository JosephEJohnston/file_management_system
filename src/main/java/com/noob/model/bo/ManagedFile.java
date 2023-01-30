package com.noob.model.bo;

import com.noob.model.po.FilePO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ManagedFile {
    private Long id;

    private String name;

    private String fullPath;

    private Integer type;

    private List<Tag> tagList;

    private ManagedFile() {
        this.tagList = new ArrayList<>();
    }

    public static ManagedFile of(FilePO po) {
        ManagedFile managedFile = new ManagedFile();

        BeanUtils.copyProperties(po, managedFile);

        return managedFile;
    }

    @Override
    public String toString() {
        return name;
    }
}
