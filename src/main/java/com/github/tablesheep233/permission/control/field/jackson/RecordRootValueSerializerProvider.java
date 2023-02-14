package com.github.tablesheep233.permission.control.field.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.github.tablesheep233.permission.control.field.jackson.annotation.JsonFieldControl;
import com.github.tablesheep233.permission.control.field.util.function.ThrowingConsumer;

import java.io.IOException;

/**
 * The type Record root value serializer provider.
 *
 * @author <a href="mailto:858916094@qq.com">tablesheep233</a>
 */
public class RecordRootValueSerializerProvider extends DefaultSerializerProvider {

    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new Record root value serializer provider.
     */
    public RecordRootValueSerializerProvider() { super(); }

    /**
     * Instantiates a new Record root value serializer provider.
     *
     * @param src the src
     */
    public RecordRootValueSerializerProvider(RecordRootValueSerializerProvider src) { super(src); }

    /**
     * Instantiates a new Record root value serializer provider.
     *
     * @param src    the src
     * @param config the config
     * @param f      the f
     */
    protected RecordRootValueSerializerProvider(SerializerProvider src, SerializationConfig config,
                                                SerializerFactory f) {
        super(src, config, f);
    }

    @Override
    public void serializeValue(JsonGenerator gen, Object value) throws IOException {
        invoke(provider -> super.serializeValue(gen, value), value);
    }

    @Override
    public void serializeValue(JsonGenerator gen, Object value, JavaType rootType) throws IOException {
        invoke(provider -> super.serializeValue(gen, value, rootType), value);
    }

    @Override
    public void serializeValue(JsonGenerator gen, Object value, JavaType rootType, JsonSerializer<Object> ser) throws IOException {
        invoke(provider -> super.serializeValue(gen, value, rootType, ser), value);
    }

    @Override
    public void serializePolymorphic(JsonGenerator gen, Object value, JavaType rootType, JsonSerializer<Object> valueSer, TypeSerializer typeSer) throws IOException {
        invoke(provider -> super.serializePolymorphic(gen, value, rootType, valueSer, typeSer), value);
    }

    private void invoke(ThrowingConsumer<RecordRootValueSerializerProvider> consumer, Object value) {
        JsonFieldControl fieldControl = value.getClass().getAnnotation(JsonFieldControl.class);
        if (fieldControl != null) {
            JacksonSerializeContext.addKey(fieldControl.name());
        }
        try {
            consumer.accept(this);
        } finally {
            JacksonSerializeContext.clear();
        }
    }

    @Override
    public DefaultSerializerProvider copy()
    {
        if (getClass() != RecordRootValueSerializerProvider.class) {
            return super.copy();
        }
        return new RecordRootValueSerializerProvider(this);
    }

    @Override
    public RecordRootValueSerializerProvider createInstance(SerializationConfig config, SerializerFactory jsf) {
        return new RecordRootValueSerializerProvider(this, config, jsf);
    }
}
