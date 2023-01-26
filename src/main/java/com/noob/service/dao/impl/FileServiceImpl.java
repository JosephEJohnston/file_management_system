package com.noob.service.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.noob.model.po.FilePO;
import com.noob.service.dao.FileService;
import com.noob.service.mapper.FileMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class FileServiceImpl
        extends ServiceImpl<FileMapper, FilePO>
        implements FileService {

    @Override
    public void add(FilePO po) {
        Optional<FilePO> select = selectByNameAndFullPath(po.getName(), po.getFullPath());
        if (select.isPresent()) {
            return;
        }

        this.save(po);
    }

    @Override
    public Optional<FilePO> selectById(long id) {
        return Optional.of(getById(id));
    }

    @Override
    public Optional<FilePO> selectByNameAndFullPath(String name, String fullPath) {
        QueryWrapper<FilePO> wrapper = new QueryWrapper<>();

        wrapper.lambda()
                .eq(FilePO::getName, name)
                .eq(FilePO::getFullPath, fullPath);

        List<FilePO> list = list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return Optional.empty();
        }

        return Optional.of(list.get(0));
    }

    @Override
    public List<FilePO> selectByNameList(List<String> nameList) {
        if (CollectionUtils.isEmpty(nameList)) {
            return Collections.emptyList();
        }

        QueryWrapper<FilePO> wrapper = new QueryWrapper<>();

        wrapper.lambda()
                .in(FilePO::getName, nameList);

        return list(wrapper);
    }

    @Override
    public List<FilePO> selectByIdList(List<Long> idList) {
        QueryWrapper<FilePO> wrapper = new QueryWrapper<>();

        wrapper.lambda()
                .in(FilePO::getId, idList);

        return list(wrapper);
    }
}
