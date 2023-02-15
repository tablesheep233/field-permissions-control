package com.github.tablesheep233.permission.control.field.metadata;

public class ArrayMetaData extends GenericMetaData {

    private MetaData actualData;

    public ArrayMetaData(String key) {
        super(key);
    }

    public ArrayMetaData(String key, MetaData actualData) {
        super(key);
        this.actualData = actualData;
    }

    @Override
    public String metaDataType() {
        return ARRAY;
    }

    public void setActualData(MetaData actualData) {
        this.actualData = actualData;
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public MetaData getActual() {
        return getRealActual(actualData);
    }

    public static MetaData getRealActual(MetaData actual) {
        if (actual.isArray() || actual.isCollection()) {
            return getRealActual(actual.getActual());
        }
        return actual;
    }
}
