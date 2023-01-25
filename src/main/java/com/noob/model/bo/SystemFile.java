package com.noob.model.bo;

import com.noob.model.enums.FileTypeEnum;
import com.noob.model.intf.BaseFile;
import lombok.Getter;

import java.io.File;

@Getter
public class SystemFile implements BaseFile {

    private File file;

    SystemFile() {

    }

    void setFile(File file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return file.getName();
    }

    @Override
    public Integer getType() {
        return FileTypeEnum.getByFile(file);
    }
}
