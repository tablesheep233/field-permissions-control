package com.github.tablesheep233.permission.control.field.policy;

import java.util.Collections;
import java.util.Map;

public interface ControlPolicyLoader {

    default Map<String, Boolean> loadFlatPolicy(String key) {
        return Collections.emptyMap();
    }

    ControlPolicyLoader EMPTY = new ControlPolicyLoader() {};
}
