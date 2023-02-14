package com.github.tablesheep233.permission.control.field.metadata;

import java.util.List;

public interface MetaData {
    String getKey();
    Class<?> getType();
    default boolean isArray() {
        return false;
    }
    default boolean isCollection() {
        return false;
    }
    default Class<?> getActualType() {
        throw new UnsupportedOperationException();
    }
    default MetaData getActual() {
        throw new UnsupportedOperationException();
    }
    default boolean isMulti() {
        return false;
    }
    default List<MetaData> getMulti() {
        throw new UnsupportedOperationException();
    }
}
