package com.noob.service.biz.impl;

import com.noob.model.bo.ManagedFile;
import com.noob.model.bo.Tag;
import com.noob.model.po.TagRelationPO;
import com.noob.service.biz.FileTagBiz;
import com.noob.service.dao.TagRelationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class FileTagBizImpl implements FileTagBiz {

    @Resource
    private TagRelationService tagRelationService;

    @Override
    public boolean relateFileToTag(ManagedFile file, Tag tag) {
        return tagRelationService.add(toPO(file, tag));
    }

    private TagRelationPO toPO(ManagedFile file, Tag tag) {
        TagRelationPO po = new TagRelationPO();

        po.setEntityId(file.getId());
        po.setTagId(tag.getId());

        return po;
    }
}
