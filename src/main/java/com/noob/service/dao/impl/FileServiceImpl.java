package com.noob.service.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.noob.model.po.FilePO;
import com.noob.service.dao.FileService;
import com.noob.service.mapper.FileMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileServiceImpl
        extends ServiceImpl<FileMapper, FilePO>
        implements FileService {

    @Override
    public List<FilePO> test() {
        return this.list();
    }
}
