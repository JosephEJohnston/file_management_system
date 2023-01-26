package com.noob.service.biz.impl;

import com.noob.service.biz.SceneLoadBiz;
import javafx.fxml.FXMLLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SceneLoadBizImpl implements SceneLoadBiz {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public FXMLLoader makeFXMLLoader(String sourceClassPath) throws IOException {
        Resource resource = new ClassPathResource(sourceClassPath);
        FXMLLoader loader = new FXMLLoader(resource.getURL());
        loader.setControllerFactory(param -> applicationContext.getBean(param));

        return loader;
    }
}
