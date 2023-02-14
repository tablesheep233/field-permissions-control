package com.github.tablesheep233.permission.control.field.util.function;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The type Throwing consumer tests.
 *
 * @author <a href="mailto:858916094@qq.com">tablesheep233</a>
 */
public class ThrowingConsumerTests {

    /**
     * Throw exception.
     */
    @Test
    void throwException() {
        ThrowingConsumer<Runnable> throwingConsumer = Runnable::run;
        Assertions.assertThrows(RuntimeException.class, () -> throwingConsumer.accept(() -> {int i = 1 / 0;}));
        Assertions.assertDoesNotThrow(() -> throwingConsumer.accept(() -> {int i = 1 / 1;}));
    }
}
