package com.noob.service.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.noob.model.po.TagPO;
import com.noob.service.dao.TagService;
import com.noob.service.mapper.TagMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, TagPO> implements TagService {

    @Override
    public void add(TagPO po) {
        Optional<TagPO> opt = selectByName(po.getName());
        if (opt.isPresent()) {
            return;
        }

        this.save(po);
    }

    @Override
    public Optional<TagPO> selectByName(String name) {
        if (StringUtils.isBlank(name)) {
            return Optional.empty();
        }

        QueryWrapper<TagPO> wrapper = new QueryWrapper<>();

        wrapper.lambda()
                .eq(TagPO::getName, name);
        List<TagPO> list = list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return Optional.empty();
        }

        return Optional.of(list.get(0));
    }

    @Override
    public List<TagPO> selectAll() {
        return list();
    }

    @Override
    public List<TagPO> selectByNameList(List<String> nameList) {
        if (CollectionUtils.isEmpty(nameList)) {
            return Collections.emptyList();
        }

        QueryWrapper<TagPO> wrapper = new QueryWrapper<>();

        wrapper.lambda()
                .in(TagPO::getName, nameList);

        return list(wrapper);
    }

    @Override
    public List<TagPO> selectByIdList(List<Long> tagIdList) {
        if (CollectionUtils.isEmpty(tagIdList)) {
            return Collections.emptyList();
        }

        QueryWrapper<TagPO> wrapper = new QueryWrapper<>();

        wrapper.lambda()
                .in(TagPO::getId, tagIdList);

        return list(wrapper);
    }
}
