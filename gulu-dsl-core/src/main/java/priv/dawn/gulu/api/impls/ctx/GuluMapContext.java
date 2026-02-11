package priv.dawn.gulu.api.impls.ctx;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/02/11/20:25
 */
public class GuluMapContext extends BaseGuluContext {

    private final Map<String, Object> map = new HashMap<>();

    @Override
    public Object getIdentifier(String path) {
        Object obj = map;
        for (String pathItem : path.split("\\.")) {
            @SuppressWarnings("unchecked")
            Object nextObj = ((Map<String, Object>) obj).get(pathItem);
            obj = nextObj;
        }
        return obj;
    }

    public void put(String path, Object val) {
        String[] splitPath = path.split("\\.");
        Map<String, Object> currentMap = map;
        for (int i = 0; i < splitPath.length - 1; i++) {
            @SuppressWarnings("unchecked")
            Map<String, Object> nextMap = (Map<String, Object>) currentMap.computeIfAbsent(splitPath[i], k -> new HashMap<>());
            currentMap = nextMap;
        }
        currentMap.put(splitPath[splitPath.length-1],val);
    }

}
