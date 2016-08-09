package com.gizwits.demo.wechat.common.utils;

import java.lang.reflect.Field;

/**
 * Created by matt on 16-5-26.
 */
public class ReflectUtil {

    public static boolean isSameType(Class clazz, String fieldName, Class targetClass) {

        Field field = getFidld(clazz, fieldName);

        if (null == field) {
            return false;
        }

        String currentType = field.getGenericType().toString();
        String targetType = targetClass.toString();
        return currentType.equals(targetType);
    }

    public static Field getFidld(Class clazz, String fieldName) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            // 如果获取不到字段，则查看有无父类
            Class superClass = clazz.getSuperclass();
            if (null != superClass) {
                field = getFidld(superClass, fieldName);
            }
            else e.printStackTrace();
        }

        return field;
    }

}
