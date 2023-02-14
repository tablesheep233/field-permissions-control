package com.github.tablesheep233.permission.control.field.metadata;

public class GenericMetaData implements MetaData {

    private final String key;

    private Class<?> type;

    public GenericMetaData(String key) {
        this.key = key;
    }

    public GenericMetaData(String key, Class<?> type) {
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

    public String getTypeName() {
        return type.getTypeName();
    }
}
