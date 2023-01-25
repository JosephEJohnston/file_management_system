package com.noob.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.noob.model.po.TagPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TagMapper extends BaseMapper<TagPO> {

}
