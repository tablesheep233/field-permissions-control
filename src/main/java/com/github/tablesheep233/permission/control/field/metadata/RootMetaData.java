package com.github.tablesheep233.permission.control.field.metadata;

import com.github.tablesheep233.permission.control.field.metadata.factory.MetaDataFactory;
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
 * @author zy.h
 */
public class RootMetaData extends GenericMetaData {
    /**
     * The constant KEY_DELIMITER.
     */
    public static final String KEY_DELIMITER = ":";

    private static final BiConsumer<MetaData, MetaDataContent> ARRAY_FILLER = (metaData, metaDataContent) -> {
        metaDataContent.setArray(true);
        MetaData actual = metaData.getActual();
        if (actual.isMulti()) {
            metaDataContent.setFields(generateFieldContent(metaDataContent.getFullKey(), actual.getMulti()));
        } else {
            metaDataContent.setType(actual.getType().getName());
        }
    };

    private static final BiConsumer<MetaData, MetaDataContent> MULTI_FILLER = (metaData, metaDataContent) -> {
        metaDataContent.setMulti(true);
        metaDataContent.setFields(generateFieldContent(metaDataContent.getFullKey(), metaData.getMulti()));
    };

    private static final BiConsumer<MetaData, MetaDataContent> UN_SUPPORT_FILLER = (metaData, metaDataContent) -> {
        throw new UnsupportedOperationException();
    };

    private static final Map<String, BiConsumer<MetaData, MetaDataContent>> FILLERS;

    static {
        FILLERS = new HashMap<>();
        FILLERS.put(GENERIC, (metaData, metaDataContent) -> metaDataContent.setType(metaData.getType().getName()));
        FILLERS.put(ARRAY, ARRAY_FILLER);
        FILLERS.put(COLLECTION, ARRAY_FILLER);
        FILLERS.put(MULTI, MULTI_FILLER);
    }

    private List<MetaData> fields;

    private MetaDataContent content;

    /**
     * Instantiates a new Root meta data.
     *
     * @param key    the key
     * @param type   the type
     * @param fields the fields
     */
    public RootMetaData(String key, Class<?> type, List<MetaData> fields) {
        super(key, type);
        this.fields = fields;
        this.content = generateContent();
    }

    @Override
    public String metaDataType() {
        return "ROOT";
    }

    private MetaDataContent generateContent() {
        String rootKey = getKey();
        MetaDataContent content = new MetaDataContent();
        content.setKey(rootKey);
        content.setMulti(true);
        content.setFields(generateFieldContent(rootKey, fields));
        return content;
    }

    private static List<MetaDataContent> generateFieldContent(String rootKey, List<MetaData> fieldMetaDatas) {
        List<MetaDataContent> list = new ArrayList<>(fieldMetaDatas.size());
        for (MetaData field : fieldMetaDatas) {
            list.add(generateFieldContent(rootKey, field));
        }
        return list;
    }

    private static MetaDataContent generateFieldContent(String rootKey, MetaData fieldMetaData) {
        String key = fieldMetaData.getKey();
        String fullKey = MetaDataFactory.PLACEHOLDER.equals(key) ? rootKey : rootKey + KEY_DELIMITER + key;

        if (KEY.equals(fieldMetaData.metaDataType())) {
            fieldMetaData = ((KeyWrapperMetaData) fieldMetaData).getTarget();
        }

        MetaDataContent content = new MetaDataContent();
        content.setKey(key);
        content.setFullKey(fullKey);

        getFiller(fieldMetaData.metaDataType()).accept(fieldMetaData, content);

        return content;
    }

    private static BiConsumer<MetaData, MetaDataContent> getFiller(String metaDataType) {
        return FILLERS.getOrDefault(metaDataType, UN_SUPPORT_FILLER);
    }

    /**
     * The type Meta data content.
     *
     * @author zy.h
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
