package com.noob.model.enums;

import java.io.File;

public enum FileTypeEnum {
    UNKNOWN(-1),
    NOT_MANAGED(0),
    FILE(1),
    // DIRECTORY(2), // 不再存储文件夹
    ;
    private final int type;

    FileTypeEnum(int type) {
        this.type = type;
    }

    public static int getByFile(File file) {
        if (file == null) {
            return UNKNOWN.type;
        }

        if (file.isFile()) {
            return FILE.type;
        }

        return NOT_MANAGED.type;
    }


    public int getType() {
        return type;
    }
}
