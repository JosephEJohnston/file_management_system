package com.noob.service.biz;

import com.noob.model.bo.ManagedFile;
import com.noob.model.bo.SystemFile;
import com.noob.model.bo.SystemNotManagedFile;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.List;
import java.util.Optional;

public interface FileBiz {
    Optional<ManagedFile> addManagedFile(File file);

    Pair<SystemNotManagedFile, List<SystemFile>> renderSystemFileDirectory(File directory);
}
