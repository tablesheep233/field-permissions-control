package com.github.tablesheep233.permission.control.field.metadata.event;

import com.github.tablesheep233.permission.control.field.metadata.storage.MetadataStorage;
import lombok.Data;

/**
 * The type Metadata prepare event.
 *
 * @author <a href="mailto:858916094@qq.com">tablesheep233</a>
 */
@Data
public class MetadataPrepareEvent {

    public MetadataPrepareEvent(MetadataStorage storage) {
        this.storage = storage;
    }

    private final MetadataStorage storage;

}
