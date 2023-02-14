package com.github.tablesheep233.permission.control.field.metadata;

import java.util.List;

public class RootMetaData extends GenericMetaData {

    private List<MetaData> fields;

    public RootMetaData(String key, Class<?> type, List<MetaData> fields) {
        super(key, type);
        this.fields = fields;
    }

    //todo merge field description
}
