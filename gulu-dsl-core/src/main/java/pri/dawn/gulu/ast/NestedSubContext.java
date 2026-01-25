package pri.dawn.gulu.ast;

import pri.dawn.gulu.tool.GuluContext;
import pri.dawn.gulu.tool.GuluReferableExpression;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/01/24/21:03
 */
public class NestedSubContext implements GuluContext {

    @Override
    public Object getIdentifier(String path) {
        return null;
    }

    @Override
    public Object getEnvVar(String path) {
        return null;
    }

    @Override
    public GuluAstNode getReferAstNode(String path) {
        return null;
    }


    @Override
    public Boolean getReferExpressionResult(String path) {
        return null;
    }

    @Override
    public void registerReferExpression(GuluReferableExpression expression) {

    }
}
