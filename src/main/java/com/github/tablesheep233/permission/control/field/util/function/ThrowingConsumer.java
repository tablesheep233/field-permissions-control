package com.github.tablesheep233.permission.control.field.util.function;

import java.util.function.Consumer;

/**
 * The interface Throwing consumer.
 *
 * @param <T> the type parameter
 * @author <a href="mailto:858916094@qq.com">tablesheep233</a>
 */
@FunctionalInterface
public interface ThrowingConsumer<T> extends Consumer<T> {

    @Override
    default void accept(T t) {
        try {
            accept0(t);
        } catch (Throwable e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException)e;
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Accept 0.
     *
     * @param t the t
     * @throws Exception the exception
     */
    void accept0(T t) throws Exception;
}
