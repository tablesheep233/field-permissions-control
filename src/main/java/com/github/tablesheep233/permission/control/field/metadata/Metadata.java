package com.github.tablesheep233.permission.control.field.metadata;

import java.util.List;

public interface Metadata {

    String GENERIC = "GENERIC";
    String ARRAY = "ARRAY";
    String COLLECTION = "COLLECTION";
    String MULTI = "MULTI";
    String KEY = "KEY";

    String getKey();
    Class<?> getType();
    String metaDataType();
    default boolean isArray() {
        return false;
    }
    default boolean isCollection() {
        return false;
    }
    default Metadata getActual() {
        throw new UnsupportedOperationException();
    }
    default boolean isMulti() {
        return false;
    }
    default List<Metadata> getMulti() {
        throw new UnsupportedOperationException();
    }
}
