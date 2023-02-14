package com.github.tablesheep233.permission.control.field.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.github.tablesheep233.permission.control.field.jackson.FieldControlPropertyFilter;
import com.github.tablesheep233.permission.control.field.jackson.JsonFieldControlAnnotationIntrospector;
import com.github.tablesheep233.permission.control.field.jackson.RecordRootValueSerializerProvider;
import com.github.tablesheep233.permission.control.field.jackson.annotation.JsonFieldControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Jackson config.
 *
 * @author <a href="mailto:858916094@qq.com">tablesheep233</a>
 */
@Configuration
@ConditionalOnBean(ObjectMapper.class)
public class JacksonConfig {

    /**
     * Configure object mapper.
     *
     * @param objectMapper the object mapper
     */
    @Autowired
    public void configureObjectMapper(ObjectMapper objectMapper) {
        objectMapper.setConfig(objectMapper.getSerializationConfig().with(new JsonFieldControlAnnotationIntrospector()))
                .setSerializerProvider(new RecordRootValueSerializerProvider())
                .setFilterProvider(new SimpleFilterProvider().addFilter(JsonFieldControl.FILTER_NAME, new FieldControlPropertyFilter()));
    }
}
