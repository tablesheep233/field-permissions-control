package com.github.tablesheep233.permission.control.field.metadata;

import com.github.tablesheep233.permission.control.field.metadata.factory.MetadataFactory;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * The type Root meta data.
 *
 * @author <a href="mailto:858916094@qq.com">tablesheep233</a>
 */
public class RootMetadata extends GenericMetadata {
    /**
     * The constant KEY_DELIMITER.
     */
    public static final String KEY_DELIMITER = ":";

    private static final BiConsumer<Metadata, MetaDataContent> ARRAY_FILLER = (metaData, metaDataContent) -> {
        metaDataContent.setArray(true);
        Metadata actual = metaData.getActual();
        if (actual.isMulti()) {
            metaDataContent.setMulti(true);
            metaDataContent.setFields(generateFieldContent(metaDataContent.getFullKey(), actual.getMulti()));
        } else {
            metaDataContent.setType(actual.getType().getName());
        }
    };

    private static final BiConsumer<Metadata, MetaDataContent> MULTI_FILLER = (metaData, metaDataContent) -> {
        metaDataContent.setMulti(true);
        metaDataContent.setFields(generateFieldContent(metaDataContent.getFullKey(), metaData.getMulti()));
    };

    private static final BiConsumer<Metadata, MetaDataContent> UN_SUPPORT_FILLER = (metaData, metaDataContent) -> {
        throw new UnsupportedOperationException();
    };

    private static final Map<String, BiConsumer<Metadata, MetaDataContent>> FILLERS;

    static {
        FILLERS = new HashMap<>();
        FILLERS.put(GENERIC, (metaData, metaDataContent) -> metaDataContent.setType(metaData.getType().getName()));
        FILLERS.put(ARRAY, ARRAY_FILLER);
        FILLERS.put(COLLECTION, ARRAY_FILLER);
        FILLERS.put(MULTI, MULTI_FILLER);
    }

    private final List<Metadata> fields;

    private final MetaDataContent content;

    /**
     * Instantiates a new Root meta data.
     *
     * @param key    the key
     * @param type   the type
     * @param fields the fields
     */
    public RootMetadata(String key, Class<?> type, List<Metadata> fields) {
        super(key, type);
        this.fields = fields;
        this.content = generateContent();
    }

    @Override
    public String metaDataType() {
        return "ROOT";
    }

    public MetaDataContent getContent() {
        return content;
    }

    private MetaDataContent generateContent() {
        String rootKey = getKey();
        MetaDataContent content = new MetaDataContent();
        content.setKey(rootKey);
        content.setMulti(true);
        content.setFields(generateFieldContent(rootKey, fields));
        return content;
    }

    private static List<MetaDataContent> generateFieldContent(String parentKey, List<Metadata> fieldMetadata) {
        List<MetaDataContent> list = new ArrayList<>(fieldMetadata.size());
        for (Metadata field : fieldMetadata) {
            list.add(generateFieldContent(parentKey, field));
        }
        return list;
    }

    private static MetaDataContent generateFieldContent(String parentKey, Metadata fieldMetadata) {
        String key = fieldMetadata.getKey();
        String fullKey = MetadataFactory.PLACEHOLDER.equals(key) ? parentKey : parentKey + KEY_DELIMITER + key;

        if (KEY.equals(fieldMetadata.metaDataType())) {
            fieldMetadata = ((KeyWrapperMetadata) fieldMetadata).getTarget();
        }

        MetaDataContent content = new MetaDataContent();
        content.setKey(key);
        content.setFullKey(fullKey);

        getFiller(fieldMetadata.metaDataType()).accept(fieldMetadata, content);

        return content;
    }

    private static BiConsumer<Metadata, MetaDataContent> getFiller(String metaDataType) {
        return FILLERS.getOrDefault(metaDataType, UN_SUPPORT_FILLER);
    }

    /**
     * The type Meta data content.
     *
     * @author <a href="mailto:858916094@qq.com">tablesheep233</a>
     */
    @Data
    public static class MetaDataContent implements Serializable {

        private String key;

        private String fullKey;

        private String type;

        private boolean array;

        private boolean multi;

        private List<MetaDataContent> fields;
    }
}
