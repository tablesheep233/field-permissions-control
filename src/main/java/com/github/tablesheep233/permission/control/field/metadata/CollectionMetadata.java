package com.github.tablesheep233.permission.control.field.metadata;

public class CollectionMetadata extends GenericMetadata {

    private Metadata actualData;

    public CollectionMetadata(String key) {
        super(key);
    }

    public CollectionMetadata(String key, Metadata actualData) {
        super(key);
        this.actualData = actualData;
    }

    @Override
    public String metaDataType() {
        return COLLECTION;
    }

    public void setActualData(Metadata actualData) {
        this.actualData = actualData;
    }

    @Override
    public boolean isCollection() {
        return true;
    }

    @Override
    public Metadata getActual() {
        return ArrayMetadata.getRealActual(actualData);
    }
}
