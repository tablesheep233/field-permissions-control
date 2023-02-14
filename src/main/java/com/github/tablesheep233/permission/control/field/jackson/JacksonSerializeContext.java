package com.github.tablesheep233.permission.control.field.jackson;

import org.springframework.util.CollectionUtils;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * The type Jackson serialize context.
 * FILO , record serialize key
 *
 * @author <a href="mailto:858916094@qq.com">tablesheep233</a>
 */
public final class JacksonSerializeContext {

    private static final ThreadLocal<Deque<String>> SERIALIZE_KEYS = ThreadLocal.withInitial(ArrayDeque::new);


    /**
     * Add key.
     *
     * @param key the key
     */
    public static void addKey(String key) {
        SERIALIZE_KEYS.get().add(key);
    }

    /**
     * Pop key string.
     *
     * @return the string
     */
    public static String popKey() {
        Deque<String> strings = SERIALIZE_KEYS.get();
        if (CollectionUtils.isEmpty(strings)) {
            return "";
        }
        return strings.removeLast();
    }

    /**
     * Clear.
     */
    public static void clear() {
        SERIALIZE_KEYS.remove();
    }

    /**
     * Key string.
     *
     * @return the string
     */
    public static String key() {
        Deque<String> strings = SERIALIZE_KEYS.get();
        if (CollectionUtils.isEmpty(strings)) {
            return "";
        }
        return String.join(":", strings);
    }
}
