package com.github.tablesheep233.permission.control.field.metadata;

public class ArrayMetadata extends GenericMetadata {

    private Metadata actualData;

    public ArrayMetadata(String key) {
        super(key);
    }

    public ArrayMetadata(String key, Metadata actualData) {
        super(key);
        this.actualData = actualData;
    }

    @Override
    public String metaDataType() {
        return ARRAY;
    }

    public void setActualData(Metadata actualData) {
        this.actualData = actualData;
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public Metadata getActual() {
        return getRealActual(actualData);
    }

    public static Metadata getRealActual(Metadata actual) {
        if (actual.isArray() || actual.isCollection()) {
            return getRealActual(actual.getActual());
        }
        return actual;
    }
}
