package com.noob.service.biz;

import com.noob.model.bo.ManagedFile;
import com.noob.model.bo.Tag;

public interface FileTagBiz {
    boolean relateFileToTag(ManagedFile file, Tag tag);
}
