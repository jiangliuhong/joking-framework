package org.jokingframework.test.utils;

import org.jokingframework.core.utils.ClassUtil;
import org.jokingframework.test.BaseTest;
import org.junit.Test;

import java.util.Set;

/**
 * @author jiangliuhong
 * @since 1.0
 */
public class TestClass extends BaseTest {

    @Test
    public void test01(){
        Set<Class<?>> classSet = ClassUtil.getClassSet("org.jokingframework.core");
        System.out.println(classSet.size());
    }


}
