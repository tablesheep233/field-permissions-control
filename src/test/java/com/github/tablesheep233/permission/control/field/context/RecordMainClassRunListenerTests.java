package com.github.tablesheep233.permission.control.field.context;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

/**
 * Tests for {@link SpringBootTest} with record spring boot app main class.
 *
 * @author <a href="mailto:858916094@qq.com">tablesheep233</a>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class RecordMainClassRunListenerTests {

    @Autowired
    private Environment environment;

    @Test
    void mainClassRecord() {
        Assertions.assertEquals(environment.getProperty(RecordMainClassRunListener.MAIN_CLASS, Class.class), RecordMainClassRunListenerTests.class);
    }
}
