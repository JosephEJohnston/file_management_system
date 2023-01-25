package com.noob.service.dao;

import com.noob.model.po.TagPO;

import java.util.List;
import java.util.Map;

public interface TagEntityService {

    Map<Long, List<TagPO>> selectTagByEntityId(List<Long> entityIdList);
}
