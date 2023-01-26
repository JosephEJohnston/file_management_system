package com.noob.model.bo;

import com.noob.model.po.TagPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;


@Getter
@Setter
public class Tag {

    private String name;

    private Tag() {

    }

    public static Tag of(String name) {
        Tag bo = new Tag();

        bo.setName(name);

        return bo;
    }

    public static Tag of(TagPO po) {
        Tag tag = new Tag();

        BeanUtils.copyProperties(po, tag);

        return tag;
    }

    @Override
    public String toString() {
        return getName();
    }
}
