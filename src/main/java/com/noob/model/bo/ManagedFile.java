package com.noob.model.bo;

import com.noob.model.po.FilePO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Getter
@Setter
@ToString
public class ManagedFile {
    private Long id;

    private String name;

    private String fullPath;

    private Integer type;

    private List<Tag> tagList;

    private ManagedFile() {

    }

    public static ManagedFile of(FilePO po) {
        ManagedFile managedFile = new ManagedFile();

        BeanUtils.copyProperties(po, managedFile);

        return managedFile;
    }
}
