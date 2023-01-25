package com.noob.service.biz.impl;

import com.noob.model.bo.*;
import com.noob.model.po.FilePO;
import com.noob.model.po.TagPO;
import com.noob.service.biz.RenderSystemFileDirectoryBiz;
import com.noob.service.dao.FileService;
import com.noob.service.dao.TagEntityService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RenderSystemFileDirectoryBizImpl implements RenderSystemFileDirectoryBiz {

    @Resource
    private FileService fileService;

    @Resource
    private TagEntityService tagEntityService;

    private final File directory;

    private List<File> fileList;

    private List<FilePO> filePOList;

    private Map<Long, List<TagPO>> entityIdToTagMap;

    /**
     * Map[name, Map[fullPath, file]]
     */
    private Map<String, Map<String, ManagedFile>> managedMap;

    private SystemNotManagedFile directorySystemFile;

    private List<SystemFile> systemFileList;

    public RenderSystemFileDirectoryBizImpl(File directory) {
        this.directory = directory;
    }

    @Override
    public RenderSystemFileDirectoryBizImpl init() {
        initFileList();
        initFilePoList();
        initTagMap();
        initManagedMap();

        return this;
    }

    private void initFileList() {
        File[] files = directory.listFiles();

        if (files != null) {
            this.fileList = new ArrayList<>(List.of(files));
        } else {
            this.fileList = Collections.emptyList();
        }
    }

    private void initFilePoList() {
        List<String> fileNameList = fileList.stream()
                .map(File::getName).toList();

        this.filePOList = fileService.selectByNameList(fileNameList);
    }

    private void initTagMap() {
        List<Long> fileIdList = filePOList.stream()
                .map(FilePO::getId).toList();

        this.entityIdToTagMap = tagEntityService.selectTagByEntityId(fileIdList);
    }

    private void initManagedMap() {
        this.managedMap = this.filePOList.stream()
                .map(ManagedFile::of)
                .collect(Collectors.groupingBy(ManagedFile::getName,
                        Collectors.toMap(ManagedFile::getFullPath, Function.identity())));
    }

    @Override
    public RenderSystemFileDirectoryBiz makeDirectorySystemFile() {
        this.directorySystemFile = SystemNotManagedFile.of(directory);

        return this;
    }

    @Override
    public RenderSystemFileDirectoryBiz makeSystemFileList() {
        this.systemFileList = fileList.stream()
                .map(this::makeSystemFile)
                .peek(this::renderSystemFileTag)
                .toList();

        return this;
    }

    private SystemFile makeSystemFile(
            File f
    ) {
        ManagedFile managedFile = Optional.ofNullable(managedMap.get(f.getName()))
                .map(innerMap -> innerMap.get(f.getAbsolutePath()))
                .orElse(null);

        if (managedFile == null) {
            return SystemNotManagedFile.of(f);
        } else {
            return SystemNormalFile.of(f, managedFile);
        }
    }

    private void renderSystemFileTag(SystemFile file) {
        if (file instanceof SystemNormalFile normalFile) {
            ManagedFile managedFile = normalFile.getManagedFile();

            List<Tag> tagList = entityIdToTagMap
                    .getOrDefault(managedFile.getId(), Collections.emptyList())
                    .stream().map(Tag::of).toList();

            managedFile.setTagList(tagList);
        }
    }

    @Override
    public Pair<SystemNotManagedFile, List<SystemFile>> result() {
        return Pair.of(directorySystemFile, systemFileList);
    }
}
