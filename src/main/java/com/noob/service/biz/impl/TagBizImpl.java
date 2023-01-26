package com.noob.service.biz.impl;

import com.noob.model.bo.Tag;
import com.noob.model.po.TagPO;
import com.noob.service.biz.TagBiz;
import com.noob.service.dao.TagService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagBizImpl implements TagBiz {

    @Resource
    private TagService tagService;

    @Override
    public Optional<Tag> addTag(String name) {
        TagPO po = toPo(name);

        tagService.add(po);

        return makeTag(po);
    }

    private TagPO toPo(String name) {
        TagPO po = new TagPO();

        po.setName(name);

        return po;
    }

    @Override
    public List<Tag> getAllTag() {
        List<TagPO> tagPOList = tagService.selectAll();

        return tagPOList.stream().map(this::makeTag)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }


    private Optional<Tag> makeTag(TagPO po) {
        if (po.getId() == null) {
            return Optional.empty();
        }

        return Optional.of(Tag.of(po));
    }
}
