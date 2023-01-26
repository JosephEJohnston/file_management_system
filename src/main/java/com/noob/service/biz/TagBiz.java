package com.noob.service.biz;

import com.noob.model.bo.Tag;

import java.util.List;
import java.util.Optional;

public interface TagBiz {
    Optional<Tag> addTag(String name);

    List<Tag> getAllTag();
}
