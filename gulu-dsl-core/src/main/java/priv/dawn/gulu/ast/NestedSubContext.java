package priv.dawn.gulu.ast;

import priv.dawn.gulu.api.GuluContext;
import priv.dawn.gulu.api.GuluContextOptions;
import priv.dawn.gulu.exception.ExpressionEvaluateException;
import priv.dawn.gulu.utils.ReflectUtils;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/01/24/21:03
 */
public class NestedSubContext implements GuluContext {

    private final GuluContext parentCtx;
    private final Object currentElement;

    public NestedSubContext(GuluContext parentCtx, Object currentElement) {
        this.parentCtx = parentCtx;
        this.currentElement = currentElement;
    }

    @Override
    public Object getIdentifier(String path) {
        return ReflectUtils.getFieldByPath(this.currentElement, path);
    }

    @Override
    public Object getEnvVar(String path) {
        return parentCtx.getEnvVar(path);
    }

    @Override
    public GuluAstNode getReferAstNode(String path) {
        throw new ExpressionEvaluateException("Nested expression can not refer to other expression!");
    }


    @Override
    public Boolean getReferExpressionResult(String path) {
        throw new ExpressionEvaluateException("Nested expression can not refer to other expression!");
    }

    @Override
    public GuluContextOptions getOptions() {
        return parentCtx.getOptions();
    }
}
