package priv.dawn.gulu.utils;

import java.lang.reflect.Field;

/**
 * @author Dawn Yang
 * @since 2026/01/27/23:01
 */
public class ReflectUtils {

    // path like user.info.email
    public static Object getFieldByPath(Object obj, String path) {
        return getFieldByPath(obj, path.split("\\."));
    }

    // path like user.info.email
    public static Object getFieldByPath(Object obj, String[] pathArr) {
        for (String pathItem : pathArr) {
            try {
                Field field = obj.getClass().getDeclaredField(pathItem);
                field.setAccessible(true);
                obj = field.get(obj);
            } catch (Exception e) {
                throw new RuntimeException("Can not get field by path:" + pathItem + "from class:" + obj.getClass());
            }
        }
        return obj;
    }

}
