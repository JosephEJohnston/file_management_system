package com.noob.service.biz.impl;

import com.noob.model.bo.ManagedFile;
import com.noob.model.bo.SystemFile;
import com.noob.model.enums.FileTypeEnum;
import com.noob.model.po.FilePO;
import com.noob.service.biz.FileBiz;
import com.noob.service.dao.FileService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FileBizImpl implements FileBiz {

    @Resource
    private FileService fileService;

    @Override
    public ManagedFile addManagedFile(File file) {
        FilePO filePO = toPO(file);

        fileService.add(filePO);

        return fileService
                .selectById(filePO.getId())
                .map(ManagedFile::of)
                .orElse(null);
    }

    @Override
    public Pair<SystemFile, List<SystemFile>> renderSystemFileDirectory(File directory) {
        Optional<FilePO> filePO = fileService.selectByNameAndFullPath(
                directory.getName(), directory.getAbsolutePath());
        SystemFile directorySystemFile = SystemFile.of(directory,
                filePO.map(ManagedFile::of).orElse(null));

        File[] files = directory.listFiles();
        if (files == null) {
            return Pair.of(directorySystemFile, Collections.emptyList());
        }

        List<File> fileList = new ArrayList<>(List.of(files));
        List<String> fileNameList = fileList.stream()
                .map(File::getName).toList();
        List<FilePO> filePOList = fileService.selectByNameList(fileNameList);
        Map<String, Map<String, ManagedFile>> managedMap = filePOList.stream()
                .map(ManagedFile::of)
                .collect(Collectors.groupingBy(ManagedFile::getName,
                        Collectors.toMap(ManagedFile::getFullPath, Function.identity())));

        List<SystemFile> systemFileList = fileList.stream().map(f -> {
            ManagedFile managedFile = Optional.ofNullable(managedMap.get(f.getName()))
                    .map(innerMap -> innerMap.get(f.getAbsolutePath()))
                    .orElse(null);

            return SystemFile.of(f, managedFile);
        }).toList();

        return Pair.of(directorySystemFile, systemFileList);
    }

    private FilePO toPO(File file) {
        FilePO filePO = new FilePO();

        filePO.setName(file.getName());
        filePO.setFullPath(file.getAbsolutePath());
        filePO.setSize(file.getTotalSpace());
        filePO.setType(FileTypeEnum.getByFile(file));

        return filePO;
    }
}
