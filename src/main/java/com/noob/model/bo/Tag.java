package com.noob.model.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class Tag {

    private String name;

    private Tag() {

    }

    public static Tag of(String name) {
        Tag bo = new Tag();

        bo.setName(name);

        return bo;
    }
}
