package com.noob.service.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.noob.model.po.TagRelationPO;
import com.noob.service.dao.TagRelationService;
import com.noob.service.mapper.TagRelationMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TagRelationServiceImpl extends ServiceImpl<TagRelationMapper, TagRelationPO> implements TagRelationService {

    @Override
    public boolean add(TagRelationPO po) {
        Optional<TagRelationPO> selectOpt =
                selectByTagIdAndEntityId(po.getTagId(), po.getEntityId());
        if (selectOpt.isPresent()) {
            return false;
        }

        save(po);

        return po.getId() != null;
    }

    @Override
    public List<TagRelationPO> selectByTagIdList(List<Long> tagIdList) {
        if (CollectionUtils.isEmpty(tagIdList)) {
            return Collections.emptyList();
        }

        QueryWrapper<TagRelationPO> wrapper = new QueryWrapper<>();

        wrapper.lambda()
                .in(TagRelationPO::getTagId, tagIdList);

        return list(wrapper);
    }

    @Override
    public List<TagRelationPO> selectByEntityIdList(List<Long> entityIdList) {
        if (CollectionUtils.isEmpty(entityIdList)) {
            return Collections.emptyList();
        }

        QueryWrapper<TagRelationPO> wrapper = new QueryWrapper<>();

        wrapper.lambda()
                .in(TagRelationPO::getEntityId, entityIdList);

        return list(wrapper);
    }

    @Override
    public Optional<TagRelationPO> selectByTagIdAndEntityId(Long tagId, Long entityId) {
        QueryWrapper<TagRelationPO> wrapper = new QueryWrapper<>();

        wrapper.lambda()
                .eq(TagRelationPO::getTagId, tagId)
                .eq(TagRelationPO::getEntityId, entityId);
        List<TagRelationPO> list = list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return Optional.empty();
        }

        return Optional.of(list.get(0));
    }
}
