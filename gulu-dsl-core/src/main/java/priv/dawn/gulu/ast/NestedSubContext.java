package priv.dawn.gulu.ast;

import priv.dawn.gulu.exception.ExpressionEvaluateException;
import priv.dawn.gulu.tool.GuluContext;
import priv.dawn.gulu.tool.GuluReferableExpression;
import priv.dawn.gulu.utils.ReflectUtils;

import java.util.List;

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
    public void registerReferExpression(GuluReferableExpression expression) {
        throw new ExpressionEvaluateException("Nested expression can not refer to other expression!");
    }

    @Override
    public List<String> getSupportedDateFormat() {
        return parentCtx.getSupportedDateFormat();
    }
}
