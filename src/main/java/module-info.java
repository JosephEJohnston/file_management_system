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

    exports com.noob to javafx.graphics, spring.beans, spring.context;
    exports com.noob.controller to spring.beans;
    exports com.noob.component to spring.beans;
    exports com.noob.service.biz.impl to spring.beans;
    exports com.noob.service.dao.impl to spring.beans;

    /*opens com.noob to spring.core;
    opens com.noob.controller to spring.core;
    opens com.noob.component to spring.core;
    opens com.noob.service.biz.impl to spring.core;
    opens com.noob.service.dao.impl to spring.core;
    opens com.noob.model.po to com.baomidou.mybatis.plus.core;*/
}