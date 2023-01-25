package com.noob.service.dao;

import com.noob.model.po.TagRelationPO;

import java.util.List;

public interface TagRelationService {
    void add(TagRelationPO po);

    List<TagRelationPO> selectByTagIdList(List<Long> tagIdList);

    List<TagRelationPO> selectByEntityIdList(List<Long> entityIdList);
}
