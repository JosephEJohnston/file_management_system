package com.noob.model.bo;

import lombok.Getter;

import java.io.File;

@Getter
public class SystemFile {

    private File file;

    private ManagedFile managedFile;

    private SystemFile() {

    }

    public static SystemFile of(File file, ManagedFile managedFile) {
        SystemFile bo = new SystemFile();

        bo.setFile(file);
        bo.setManagedFile(managedFile);

        return bo;
    }

    private void setFile(File file) {
        this.file = file;
    }

    public void setManagedFile(ManagedFile managedFile) {
        this.managedFile = managedFile;
    }

    @Override
    public String toString() {
        return file.getName();
    }
}
