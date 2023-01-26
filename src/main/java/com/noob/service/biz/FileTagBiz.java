package com.noob.service.biz;

import com.noob.model.bo.ManagedFile;
import com.noob.model.bo.Tag;

import java.util.List;

public interface FileTagBiz {
    boolean relateFileToTag(ManagedFile file, Tag tag);

    List<ManagedFile> searchFileByTagList(List<Tag> tagList);
}
