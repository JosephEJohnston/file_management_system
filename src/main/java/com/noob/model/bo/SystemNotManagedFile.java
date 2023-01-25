package com.noob.model.bo;

import com.noob.model.enums.FileTypeEnum;

import java.io.File;

public class SystemNotManagedFile extends SystemFile {

    private SystemNotManagedFile() {
        super();
    }

    public static SystemNotManagedFile of(File file) {
        SystemNotManagedFile directory = new SystemNotManagedFile();

        directory.setFile(file);

        return directory;
    }

    @Override
    public Integer getType() {
        return FileTypeEnum.NOT_MANAGED.getType();
    }
}
