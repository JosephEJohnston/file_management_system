package com.noob.service.biz.impl;

import com.noob.model.bo.ManagedFile;
import com.noob.model.bo.Tag;
import com.noob.model.po.TagPO;
import com.noob.model.po.TagRelationPO;
import com.noob.service.biz.FileTagBiz;
import com.noob.service.dao.FileService;
import com.noob.service.dao.TagEntityService;
import com.noob.service.dao.TagRelationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FileTagBizImpl implements FileTagBiz {

    @Resource
    private TagRelationService tagRelationService;

    @Resource
    private TagEntityService tagEntityService;

    @Resource
    private FileService fileService;

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

    @Override
    public List<ManagedFile> searchFileByTagList(List<Tag> tagList) {
        List<Long> tagIdList = tagList.stream().map(Tag::getId).toList();

        List<Long> fileIdList = tagRelationService.selectByTagIdList(tagIdList)
                .stream()
                .map(TagRelationPO::getEntityId).toList();

        Map<Long, List<TagPO>> entityToTagListMap = tagEntityService
                .selectTagByEntityId(fileIdList);

        return fileService.selectByIdList(fileIdList)
                .stream()
                .map(ManagedFile::of)
                .peek(file -> {
                    List<Tag> fileTagList = entityToTagListMap
                            .getOrDefault(file.getId(), new ArrayList<>())
                            .stream().map(Tag::of)
                            .collect(Collectors.toList());

                    file.setTagList(fileTagList);
                })
                .collect(Collectors.toList());
    }
}
