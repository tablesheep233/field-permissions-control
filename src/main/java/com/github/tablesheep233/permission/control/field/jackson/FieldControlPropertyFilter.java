package com.github.tablesheep233.permission.control.field.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.github.tablesheep233.permission.control.field.policy.AllowPredicate;

/**
 * The type Field control property filter.
 *
 * @author <a href="mailto:858916094@qq.com">tablesheep233</a>
 */
public class FieldControlPropertyFilter extends SimpleBeanPropertyFilter {

    @Override
    public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer) throws Exception {
        JacksonSerializeContext.addKey(writer.getName());
        super.serializeAsField(pojo, jgen, provider, writer);
        JacksonSerializeContext.popKey();
    }

    @Override
    protected boolean include(PropertyWriter writer) {
        String key = JacksonSerializeContext.key();
        return super.include(writer) && AllowPredicate.allow(JacksonSerializeContext.policy(), key);
    }

    @Override
    protected boolean include(BeanPropertyWriter writer) {
        String key = JacksonSerializeContext.key();
        return super.include(writer) && AllowPredicate.allow(JacksonSerializeContext.policy(), key);
    }
}
