package com.github.tablesheep233.permission.control.field.policy;

import java.util.Map;

/**
 * The type Allow predicate.
 *
 * @author <a href="mailto:858916094@qq.com">tablesheep233</a>
 */
public class AllowPredicate {

    /**
     * Allow boolean.
     *
     * @param flatPolicy the flat policy
     * @param key        the key
     * @return the boolean
     */
    public static boolean allow(Map<String, Boolean> flatPolicy, String key) {
        return !flatPolicy.containsKey(key) || flatPolicy.get(key);
    }
}
