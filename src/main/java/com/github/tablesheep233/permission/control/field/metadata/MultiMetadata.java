package com.github.tablesheep233.permission.control.field.metadata;

import java.util.List;

public class MultiMetadata extends GenericMetadata {

    private List<Metadata> multiData;

    public MultiMetadata(String key, Class<?> type) {
        super(key, type);
    }

    @Override
    public String metaDataType() {
        return MULTI;
    }

    public void setMultiData(List<Metadata> multiData) {
        this.multiData = multiData;
    }

    @Override
    public boolean isMulti() {
        return true;
    }

    @Override
    public List<Metadata> getMulti() {
        return multiData;
    }
}
