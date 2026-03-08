package priv.dawn.gulu.api.impls.ctx;

import priv.dawn.gulu.anotions.GuluCtxPath;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 性能更好的上下文工具
 * 只能通过 @GuluCtxPath 指定注册的方法获取值
 *
 * @author Dawn Yang
 * @since 2026/02/11/20:22
 */
public class GuluGetterContext extends BaseGuluContext {

    private static final Map<Class<?>, Map<String, MethodHandle>> handleCache = new ConcurrentHashMap<>();
    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();

    private final Object ctxObj;

    public GuluGetterContext(Object obj) {
        this.ctxObj = obj;
    }

    @Override
    protected Object getValueByPath(String path) throws Exception {
        String[] splitPath = path.split("\\.");
        Object obj = this.ctxObj;
        for (String pathItem : splitPath) {
            if (obj == null) {
                throw new RuntimeException("Full path:" + path + ", the path:" + pathItem + " in context is null");
            }
            MethodHandle handler = getHandler(obj.getClass(), pathItem);
            try {
                obj = handler.invoke(obj);
            } catch (Throwable e) {
                throw new RuntimeException("MethodHandle.invokeExact failed, Class:" + obj.getClass() + ", path:" + pathItem, e);
            }
        }
        return obj;
    }

    private MethodHandle getHandler(Class<?> clazz, String path) {
        return handleCache.computeIfAbsent(clazz, GuluGetterContext::findAllSpecifiedGetter).get(path);
    }

    private static Map<String, MethodHandle> findAllSpecifiedGetter(Class<?> clazz) {
        Map<String, MethodHandle> result = new HashMap<>();
        for (Method method : clazz.getDeclaredMethods()) {
            GuluCtxPath ctxPath = method.getAnnotation(GuluCtxPath.class);
            if (ctxPath == null) {
                continue;
            }
            String path = ctxPath.value();
            try {
                MethodHandle handle = lookup.unreflect(method);
                result.put(path, handle);
            } catch (IllegalAccessException e) {
                // ...
            }
        }
        return result;
    }

}
