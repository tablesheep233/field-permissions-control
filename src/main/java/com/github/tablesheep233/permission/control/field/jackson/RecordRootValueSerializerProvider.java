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
import com.github.tablesheep233.permission.control.field.policy.ControlPolicyLoader;
import com.github.tablesheep233.permission.control.field.util.function.ThrowingConsumer;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

/**
 * The type Record root value serializer provider.
 *
 * @author <a href="mailto:858916094@qq.com">tablesheep233</a>
 */
public class RecordRootValueSerializerProvider extends DefaultSerializerProvider {

    private static final long serialVersionUID = 1L;

    private final ControlPolicyLoader policyLoader;

    /**
     * Instantiates a new Record root value serializer provider.
     * @param policyLoader
     */
    public RecordRootValueSerializerProvider(ControlPolicyLoader policyLoader) {
        super();
        this.policyLoader = policyLoader;
    }

    /**
     * Instantiates a new Record root value serializer provider.
     *
     * @param src the src
     * @param policyLoader
     */
    public RecordRootValueSerializerProvider(RecordRootValueSerializerProvider src, ControlPolicyLoader policyLoader) {
        super(src);
        this.policyLoader = policyLoader;
    }

    /**
     * Instantiates a new Record root value serializer provider.
     *  @param src    the src
     * @param config the config
     * @param f      the f
     * @param policyLoader
     */
    protected RecordRootValueSerializerProvider(SerializerProvider src, SerializationConfig config,
                                                SerializerFactory f, ControlPolicyLoader policyLoader) {
        super(src, config, f);
        this.policyLoader = policyLoader;
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
        String rootKey = resolveRootKey(value);
        if (StringUtils.hasText(rootKey)) {
            JacksonSerializeContext.setPolicy(policyLoader.loadFlatPolicy(rootKey));
            JacksonSerializeContext.addKey(rootKey);
        }
        try {
            consumer.accept(this);
        } finally {
            JacksonSerializeContext.clear();
        }
    }

    private String resolveRootKey(Object value) {
        JsonFieldControl annotation = value.getClass().getAnnotation(JsonFieldControl.class);
        if (annotation != null) {
            return annotation.name();
        }

        if (Collection.class.isAssignableFrom(value.getClass())) {
            if (!CollectionUtils.isEmpty((Collection<?>) value)) {
                //if ser collection, element type is the first not null element
                return resolveRootKey(((Collection)value).stream().filter(Objects::nonNull).findFirst().get());
            }
        }

        if (value.getClass().isArray()) {
            annotation  = value.getClass().getComponentType().getAnnotation(JsonFieldControl.class);
            if (annotation != null) {
                return annotation.name();
            }
        }
        return "";
    }

    @Override
    public DefaultSerializerProvider copy()
    {
        if (getClass() != RecordRootValueSerializerProvider.class) {
            return super.copy();
        }
        return new RecordRootValueSerializerProvider(this, policyLoader);
    }

    @Override
    public RecordRootValueSerializerProvider createInstance(SerializationConfig config, SerializerFactory jsf) {
        return new RecordRootValueSerializerProvider(this, config, jsf, policyLoader);
    }
}
