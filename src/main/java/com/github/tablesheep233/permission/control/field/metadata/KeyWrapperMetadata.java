package com.github.tablesheep233.permission.control.field.metadata;

public class KeyWrapperMetadata implements Metadata {

    private String key;

    private Metadata target;

    public KeyWrapperMetadata(String key, Metadata target) {
        this.key = key;
        this.target = target;
    }

    @Override
    public String metaDataType() {
        return KEY;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Class<?> getType() {
        return target.getType();
    }

    public Metadata getTarget() {
        return target;
    }
}
