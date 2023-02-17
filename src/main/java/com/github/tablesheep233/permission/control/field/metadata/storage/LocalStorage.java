package com.github.tablesheep233.permission.control.field.metadata.storage;

import com.github.tablesheep233.permission.control.field.metadata.RootMetadata;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class LocalStorage implements MetadataStorage {

    private final Map<String, RootMetadata.MetaDataContent> storage = new HashMap<>();

    @Override
    public void storage(RootMetadata metadata) {
        storage.put(metadata.getKey(), metadata.getContent());
    }

    @Override
    public RootMetadata.MetaDataContent load(String key) {
        return storage.get(key);
    }
}
