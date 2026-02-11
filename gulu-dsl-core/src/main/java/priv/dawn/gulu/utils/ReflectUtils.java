package priv.dawn.gulu.utils;

import java.lang.reflect.Field;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/01/27/23:01
 */
public class ReflectUtils {

    // path like user.info.email
    public static Object getFieldByPath(Object obj,String path){
        String[] pathArr = path.split("\\.");
        for (String pathItem : pathArr) {
            try {
                Field field = obj.getClass().getDeclaredField(pathItem);
                field.setAccessible(true);
                obj = field.get(obj);
            } catch (Exception e) {
                throw new RuntimeException("Can not get field by path:" + path);
            }
        }
        return obj;
    }

}
