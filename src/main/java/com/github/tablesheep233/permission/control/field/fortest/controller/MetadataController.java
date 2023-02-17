package com.github.tablesheep233.permission.control.field.fortest.controller;

import com.github.tablesheep233.permission.control.field.metadata.RootMetadata;
import com.github.tablesheep233.permission.control.field.metadata.storage.MetadataStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("metadata")
@RequiredArgsConstructor
public class MetadataController {

    private final MetadataStorage storage;

    @GetMapping("{key}")
    public RootMetadata.MetaDataContent load(@PathVariable("key") String key) {
        return storage.load(key);
    }

    @PutMapping("{key}")
    public void configUserControl(@PathVariable("key") String key) {

    }
}
