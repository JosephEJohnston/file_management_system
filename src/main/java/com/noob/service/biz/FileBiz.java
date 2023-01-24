package com.noob.service.biz;

import com.noob.model.bo.ManagedFile;
import com.noob.model.bo.SystemFile;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.List;

public interface FileBiz {
    ManagedFile addManagedFile(File file);

    Pair<SystemFile, List<SystemFile>> renderSystemFileDirectory(File directory);
}
