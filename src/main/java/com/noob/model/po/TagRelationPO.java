package com.noob.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@TableName("tag_relation")
public class TagRelationPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long tag_id;

    private Long entity_id;

    private Integer status;

    private Date createTime;

    private Date modifiedTime;
}
