package com.noob.service.dao;

import com.noob.model.po.TagRelationPO;

import java.util.List;
import java.util.Optional;

public interface TagRelationService {
    boolean add(TagRelationPO po);

    List<TagRelationPO> selectByTagIdList(List<Long> tagIdList);

    List<TagRelationPO> selectByEntityIdList(List<Long> entityIdList);

    Optional<TagRelationPO> selectByTagIdAndEntityId(Long tagId, Long entityId);
}
