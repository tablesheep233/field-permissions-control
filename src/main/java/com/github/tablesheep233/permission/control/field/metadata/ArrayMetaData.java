package com.github.tablesheep233.permission.control.field.metadata;

public class ArrayMetaData extends GenericMetaData {

    private Class<?> actualType;

    private MetaData actualData;

    public ArrayMetaData(String key, Class<?> actualType) {
        super(key);
        this.actualType = actualType;
    }

    public ArrayMetaData(String key, Class<?> actualType, MetaData actualData) {
        super(key);
        this.actualType = actualType;
        this.actualData = actualData;
    }

    public void setActualData(MetaData actualData) {
        this.actualData = actualData;
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public Class<?> getActualType() {
        return actualType;
    }

    @Override
    public MetaData getActual() {
        return actualData;
    }
}
