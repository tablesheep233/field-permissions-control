package com.github.tablesheep233.permission.control.field.metadata;

import com.github.tablesheep233.permission.control.field.jackson.annotation.JsonFieldControl;
import com.github.tablesheep233.permission.control.field.metadata.event.MetadataPrepareEvent;
import com.github.tablesheep233.permission.control.field.metadata.factory.MetadataFactory;
import com.github.tablesheep233.permission.control.field.metadata.storage.MetadataStorage;
import lombok.RequiredArgsConstructor;
import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Set;

import static com.github.tablesheep233.permission.control.field.context.RecordMainClassRunListener.MAIN_CLASS;

@Component
@RequiredArgsConstructor
public class MetadataScanner implements EnvironmentAware, ApplicationContextAware, CommandLineRunner {

    private Environment environment;

    private ApplicationContext applicationContext;

    private final MetadataFactory factory;

    private final MetadataStorage storage;

    @Override
    public void run(String... args) throws Exception {
        Reflections reflections = new Reflections(getScanPackage());
        Set<Class<?>> controlClasses = reflections.getTypesAnnotatedWith(JsonFieldControl.class);
        for (Class<?> controlClass : controlClasses) {
            String name = controlClass.getAnnotation(JsonFieldControl.class).name();
            if (StringUtils.hasText(name)) {
                storage.storage(factory.create(name, controlClass));
            }
        }
        applicationContext.publishEvent(new MetadataPrepareEvent(storage));
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    private String getScanPackage() {
        Class<?> mainClass = Objects.requireNonNull(environment.getProperty(MAIN_CLASS, Class.class));
        return ClassUtils.getPackageName(mainClass);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
