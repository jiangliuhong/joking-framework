package org.jokingframework.core.utils;

/**
 * @author jiangliuhong
 * @since 1.0
 */
public class Assert {

    public static void notNull(Object object){
        if(object == null){
            throw new NullPointerException();
        }
    }
}
