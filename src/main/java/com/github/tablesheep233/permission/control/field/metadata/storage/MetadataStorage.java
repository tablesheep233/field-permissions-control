package com.github.tablesheep233.permission.control.field.metadata.storage;

import com.github.tablesheep233.permission.control.field.metadata.RootMetadata;

public interface MetadataStorage {

    void storage(RootMetadata metadata);

    RootMetadata.MetaDataContent load(String key);
}
