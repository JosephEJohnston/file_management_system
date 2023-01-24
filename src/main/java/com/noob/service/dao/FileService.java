package com.noob.service.dao;

import com.noob.model.po.FilePO;

import java.util.List;
import java.util.Optional;

public interface FileService {

    void add(FilePO po);

    Optional<FilePO> selectById(long id);

    Optional<FilePO> selectByNameAndFullPath(String name, String fullPath);

    List<FilePO> selectByNameList(List<String> nameList);
}
