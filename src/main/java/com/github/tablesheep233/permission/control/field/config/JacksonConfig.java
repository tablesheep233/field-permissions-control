package com.github.tablesheep233.permission.control.field.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.github.tablesheep233.permission.control.field.jackson.FieldControlPropertyFilter;
import com.github.tablesheep233.permission.control.field.jackson.JsonFieldControlAnnotationIntrospector;
import com.github.tablesheep233.permission.control.field.jackson.RecordRootValueSerializerProvider;
import com.github.tablesheep233.permission.control.field.jackson.annotation.JsonFieldControl;
import com.github.tablesheep233.permission.control.field.policy.ControlPolicyLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * The type Jackson config.
 *
 * @author <a href="mailto:858916094@qq.com">tablesheep233</a>
 */
@Configuration
public class JacksonConfig {

    @Autowired
    private ControlPolicyLoader policyLoader;

    /**
     * Configure object mapper.
     *
     * @param objectMapper the object mapper
     */
    @Autowired
    public void configureObjectMapper(ObjectMapper objectMapper) {
        objectMapper.setConfig(objectMapper.getSerializationConfig().with(new JsonFieldControlAnnotationIntrospector()))
                .setSerializerProvider(new RecordRootValueSerializerProvider(policyLoader))
                .setFilterProvider(new SimpleFilterProvider().addFilter(JsonFieldControl.FILTER_NAME, new FieldControlPropertyFilter()));
    }
}
