package com.github.tablesheep233.permission.control.field.metadata;

import java.util.List;

public class MultiMetaData extends GenericMetaData {

    private List<MetaData> multiData;

    public MultiMetaData(String key, Class<?> type) {
        super(key, type);
    }

    @Override
    public String metaDataType() {
        return MULTI;
    }

    public void setMultiData(List<MetaData> multiData) {
        this.multiData = multiData;
    }

    @Override
    public boolean isMulti() {
        return true;
    }

    @Override
    public List<MetaData> getMulti() {
        return multiData;
    }
}
