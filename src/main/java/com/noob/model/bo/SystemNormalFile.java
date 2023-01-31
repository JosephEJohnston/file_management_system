package com.noob.model.bo;

import com.noob.model.enums.FileTypeEnum;

import java.io.File;

public class SystemNormalFile extends SystemFile {
    private ManagedFile managedFile;

    private SystemNormalFile() {
        super();
    }

    public static SystemNormalFile of(
            File file,
            ManagedFile managedFile
    ) {

        SystemNormalFile normalFile = new SystemNormalFile();

        normalFile.setFile(file);
        normalFile.setManagedFile(managedFile);

        return normalFile;
    }

    public static SystemNormalFile of(
            ManagedFile managedFile
    ) {

        SystemNormalFile normalFile = new SystemNormalFile();

        normalFile.setFile(new File(managedFile.getFullPath()));
        normalFile.setManagedFile(managedFile);

        return normalFile;
    }

    public ManagedFile getManagedFile() {
        return managedFile;
    }

    private void setManagedFile(ManagedFile managedFile) {
        this.managedFile = managedFile;
    }

    @Override
    public Integer getType() {
        return FileTypeEnum.FILE.getType();
    }
}
