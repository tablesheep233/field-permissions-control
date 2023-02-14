package com.github.tablesheep233.permission.control.field.jackson.annotation;

import com.fasterxml.jackson.annotation.JsonFilter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Json field control.
 *
 * @author <a href="mailto:858916094@qq.com">tablesheep233</a>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@com.fasterxml.jackson.annotation.JacksonAnnotation
@JsonFilter(value = JsonFieldControl.FILTER_NAME)
public @interface JsonFieldControl {

    /**
     * The constant FILTER_NAME.
     */
    String FILTER_NAME = "FIELD_CONTROL";

    /**
     * Name string.
     *
     * @return the string
     */
    String name() default "";
}
