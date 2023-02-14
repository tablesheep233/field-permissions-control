package com.github.tablesheep233.permission.control.field.metadata;

public class CollectionMetaData extends GenericMetaData {

    private Class<?> actualType;

    private MetaData actualData;

    public CollectionMetaData(String key, Class<?> actualType) {
        super(key);
        this.actualType = actualType;
    }

    public void setActualData(MetaData actualData) {
        this.actualData = actualData;
    }

    @Override
    public boolean isCollection() {
        return true;
    }

    @Override
    public Class<?> getActualType() {
        return actualType;
    }

    public String getActualTypeName() {
        return actualType.getTypeName();
    }

    @Override
    public MetaData getActual() {
        return actualData;
    }
}
