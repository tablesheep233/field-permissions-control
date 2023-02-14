package com.github.tablesheep233.permission.control.field.context;

import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.Map;

/**
 * record Spring Boot application main class run listener .
 *
 * @author <a href="mailto:858916094@qq.com">tablesheep233</a>
 */
public class RecordMainClassRunListener implements SpringApplicationRunListener {

    private static final String INNER_SOURCE = "app-inner-source";

    public static final String MAIN_CLASS = "main-class";

    private final SpringApplication application;

    private final String[] args;

    /**
     * Instantiates a new Record main class run listener.
     *
     * @param application the application
     * @param args        the args
     */
    public RecordMainClassRunListener(SpringApplication application, String[] args) {
        this.application = application;
        this.args = args;
    }

    @Override
    public void environmentPrepared(ConfigurableBootstrapContext bootstrapContext, ConfigurableEnvironment environment) {
        environment.getPropertySources().addFirst(new MapPropertySource(INNER_SOURCE, Map.of(MAIN_CLASS, application.getMainApplicationClass())));
    }
}
