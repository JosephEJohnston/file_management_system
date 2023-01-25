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

@Service
public class TagRelationServiceImpl extends ServiceImpl<TagRelationMapper, TagRelationPO> implements TagRelationService {

    @Override
    public void add(TagRelationPO po) {
        save(po);
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
}
