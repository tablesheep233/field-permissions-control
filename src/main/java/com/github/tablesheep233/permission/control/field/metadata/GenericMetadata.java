package com.github.tablesheep233.permission.control.field.metadata;

public class GenericMetadata implements Metadata {

    private final String key;

    private Class<?> type;

    public GenericMetadata(String key) {
        this.key = key;
    }

    public GenericMetadata(String key, Class<?> type) {
        this.key = key;
        this.type = type;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public String metaDataType() {
        return GENERIC;
    }

    public String getTypeName() {
        return type.getTypeName();
    }
}