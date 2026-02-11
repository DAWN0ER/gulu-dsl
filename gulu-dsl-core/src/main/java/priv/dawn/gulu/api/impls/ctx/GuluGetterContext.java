package priv.dawn.gulu.api.impls.ctx;

/**
 * @author Dawn Yang
 * @since 2026/02/11/20:22
 */
public class GuluGetterContext extends BaseGuluContext {

    // TODO 通过方法反射+缓存获取 value，性能比普通反射要好

    @Override
    protected Object getValueByPath(String path) throws Exception {
        return null;
    }


}
