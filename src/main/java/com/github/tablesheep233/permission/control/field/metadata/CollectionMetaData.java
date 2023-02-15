package com.github.tablesheep233.permission.control.field.metadata;

public class CollectionMetaData extends GenericMetaData {

    private MetaData actualData;

    public CollectionMetaData(String key) {
        super(key);
    }

    public CollectionMetaData(String key, MetaData actualData) {
        super(key);
        this.actualData = actualData;
    }

    @Override
    public String metaDataType() {
        return COLLECTION;
    }

    public void setActualData(MetaData actualData) {
        this.actualData = actualData;
    }

    @Override
    public boolean isCollection() {
        return true;
    }

    @Override
    public MetaData getActual() {
        return ArrayMetaData.getRealActual(actualData);
    }
}
