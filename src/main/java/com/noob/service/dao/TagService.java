package com.noob.service.dao;

import com.noob.model.po.TagPO;

import java.util.List;
import java.util.Optional;

public interface TagService {

    void add(TagPO po);

    Optional<TagPO> selectByName(String name);

    List<TagPO> selectAll();

    List<TagPO> selectByNameList(List<String> nameList);

    List<TagPO> selectByIdList(List<Long> tagIdList);
}
