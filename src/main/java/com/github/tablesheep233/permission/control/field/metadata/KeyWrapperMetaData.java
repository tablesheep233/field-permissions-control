package com.github.tablesheep233.permission.control.field.metadata;

public class KeyWrapperMetaData implements MetaData {

    private String key;

    private MetaData target;

    public KeyWrapperMetaData(String key, MetaData target) {
        this.key = key;
        this.target = target;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Class<?> getType() {
        return target.getType();
    }
}
