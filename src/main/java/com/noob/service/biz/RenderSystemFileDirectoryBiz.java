package com.noob.service.biz;


import com.noob.model.bo.SystemFile;
import com.noob.model.bo.SystemNotManagedFile;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface RenderSystemFileDirectoryBiz {

    RenderSystemFileDirectoryBiz init();

    RenderSystemFileDirectoryBiz makeDirectorySystemFile();

    RenderSystemFileDirectoryBiz makeSystemFileList();

    Pair<SystemNotManagedFile, List<SystemFile>> result();
}
