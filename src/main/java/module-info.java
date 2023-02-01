open module file.management.system {
    requires java.sql;

    requires jakarta.annotation;

    requires javafx.controls;
    requires javafx.fxml;

    requires static lombok;

    requires spring.core;
    requires spring.beans;
    requires spring.context;
    requires spring.boot;
    requires spring.boot.autoconfigure;

    requires com.baomidou.mybatis.plus.core;
    requires com.baomidou.mybatis.plus.annotation;
    requires com.baomidou.mybatis.plus.extension;
    requires org.mybatis;

    requires org.apache.commons.lang3;
    requires org.apache.commons.collections4;

    // 仅仅是为了消除 idea 报错
    exports com.noob.component.config to spring.beans;
    exports com.noob.model.bo to spring.beans;
    exports com.noob.model.po to spring.beans;
    exports com.noob.service.biz to spring.beans;

    // 实际需要 exports 的包
    exports com.noob to javafx.graphics, spring.beans, spring.context;
    exports com.noob.controller to spring.beans;
    exports com.noob.component to spring.beans;
    exports com.noob.service.biz.impl to spring.beans;
    exports com.noob.service.dao.impl to spring.beans;
}