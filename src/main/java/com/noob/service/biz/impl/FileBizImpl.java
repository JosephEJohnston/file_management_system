package com.noob.service.biz.impl;

import com.noob.model.bo.ManagedFile;
import com.noob.model.bo.SystemNotManagedFile;
import com.noob.model.bo.SystemFile;
import com.noob.model.enums.FileTypeEnum;
import com.noob.model.po.FilePO;
import com.noob.service.biz.FileBiz;
import com.noob.service.biz.RenderSystemFileDirectoryBiz;
import com.noob.service.dao.FileService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
public class FileBizImpl implements FileBiz {

    @Resource
    private FileService fileService;

    @Resource
    private ApplicationContext applicationContext;

    @Override
    public Optional<ManagedFile> addManagedFile(File file) {
        FilePO filePO = toPO(file);

        fileService.add(filePO);

        return Optional.of(filePO)
                .map(FilePO::getId)
                .flatMap(fileService::selectById)
                .map(ManagedFile::of);
    }

    private FilePO toPO(File file) {
        FilePO filePO = new FilePO();

        filePO.setName(file.getName());
        filePO.setFullPath(file.getAbsolutePath());
        filePO.setSize(file.length());
        filePO.setType(FileTypeEnum.getByFile(file));

        return filePO;
    }

    @Override
    public boolean updateFile(ManagedFile file) {
        return fileService.update(toPO(file));
    }

    private FilePO toPO(ManagedFile file) {
        FilePO po = new FilePO();

        BeanUtils.copyProperties(file, po);
        po.setName(getName(file));
        po.setFullPath(getFullPath(file));

        return po;
    }

    private String getName(ManagedFile file) {
        return StringUtils.isNotEmpty(file.getName()) ? file.getName() : null;
    }

    private String getFullPath(ManagedFile file) {
        return StringUtils.isNotEmpty(file.getFullPath()) ? file.getFullPath() : null;
    }

    @Override
    public Pair<SystemNotManagedFile, List<SystemFile>> renderSystemFileDirectory(File directory) {
        RenderSystemFileDirectoryBiz renderBiz = applicationContext
                .getBean(RenderSystemFileDirectoryBizImpl.class, directory);

        return renderBiz.init()
                .makeDirectorySystemFile()
                .makeSystemFileList()
                .result();
    }

}
