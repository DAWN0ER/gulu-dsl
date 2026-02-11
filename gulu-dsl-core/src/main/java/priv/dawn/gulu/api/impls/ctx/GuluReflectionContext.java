package priv.dawn.gulu.api.impls.ctx;

import priv.dawn.gulu.utils.ReflectUtils;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/01/27/23:13
 */
public class GuluReflectionContext extends BaseGuluContext {

    // base object
    private final Object object;

    public GuluReflectionContext(Object object) {
        this.object = object;
    }


    @Override
    protected Object getValueByPath(String path) throws Exception {
        return ReflectUtils.getFieldByPath(object, path);
    }
}
