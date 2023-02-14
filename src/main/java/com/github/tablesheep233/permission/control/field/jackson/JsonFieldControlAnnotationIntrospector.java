package com.github.tablesheep233.permission.control.field.jackson;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.github.tablesheep233.permission.control.field.jackson.annotation.JsonFieldControl;

/**
 * {@link AnnotationIntrospector} implementation that handles {@link JsonFieldControl}.
 *
 * @author <a href="mailto:858916094@qq.com">tablesheep233</a>
 */
public class JsonFieldControlAnnotationIntrospector extends JacksonAnnotationIntrospector {

    @Override
    public Object findFilterId(Annotated a) {
        JsonFieldControl accessControl = super._findAnnotation(a, JsonFieldControl.class);
        if (accessControl != null) {
            return JsonFieldControl.FILTER_NAME;
        }
        return super.findFilterId(a);
    }
}
