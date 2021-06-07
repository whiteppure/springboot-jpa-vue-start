package com.github.springboot.template.utils;


import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * 公共工具类
 */
public class ObjectUtils {

    private ObjectUtils() {
    }

    /**
     * 是否为空
     *
     * @param obj 任意对象
     * @return true 为空 false 不为空
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        } else if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        } else if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        } else {
            return obj instanceof Map && ((Map) obj).isEmpty();
        }
    }

    /**
     * 是否不为空
     *
     * @param obj 任意对象
     * @return true 不为空 false 为空
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 是否为 null
     *
     * @param object 任意对象
     * @return true 为null false 不为null
     */
    public static boolean isNull(Object object) {
        return object == null;
    }

    /**
     * 是否不为 null
     *
     * @param object 任意对象
     * @return true 不为null false 为null
     */
    public static boolean isNotNull(Object object) {
        return !isNull(object);
    }


}
