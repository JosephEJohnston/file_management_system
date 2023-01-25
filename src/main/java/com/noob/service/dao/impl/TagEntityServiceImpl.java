package com.noob.service.dao.impl;

import com.noob.model.po.TagPO;
import com.noob.model.po.TagRelationPO;
import com.noob.service.dao.TagEntityService;
import com.noob.service.dao.TagRelationService;
import com.noob.service.dao.TagService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TagEntityServiceImpl implements TagEntityService {

    @Resource
    private TagService tagService;

    @Resource
    private TagRelationService tagRelationService;

    @Override
    public Map<Long, List<TagPO>> selectTagByEntityId(List<Long> entityIdList) {
        List<TagRelationPO> tagRelationPOList = tagRelationService
                .selectByEntityIdList(entityIdList);

        Map<Long, TagPO> tagMap = makeTagMap(tagRelationPOList);

        return tagRelationPOList.stream()
                .map(r -> Pair.of(r.getEntityId(), tagMap.get(r.getTagId())))
                .collect(Collectors.groupingBy(Pair::getKey,
                        Collectors.mapping(Pair::getValue, Collectors.toList())));
    }

    private Map<Long, TagPO> makeTagMap(List<TagRelationPO> tagRelationPOList) {
        List<Long> tagIdList = tagRelationPOList.stream()
                .map(TagRelationPO::getTagId)
                .toList();

        return tagService.selectByIdList(tagIdList)
                .stream()
                .collect(Collectors.toMap(TagPO::getId, Function.identity()));
    }
}
