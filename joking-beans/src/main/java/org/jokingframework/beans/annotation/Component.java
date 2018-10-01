package org.jokingframework.beans.annotation;

import java.lang.annotation.*;

/**
 * @author jiangliuhong
 * @since 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {
    String value() default "";
}
