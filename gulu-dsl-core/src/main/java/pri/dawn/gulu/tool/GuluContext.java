package pri.dawn.gulu.tool;

import pri.dawn.gulu.ast.GuluAstNode;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/01/24/16:39
 */
public interface GuluContext {

    Object getIdentifier(String path);

    Object getEnvVar(String path);

    GuluAstNode getReferAstNode(String path);

    Boolean getReferExpressionResult(String path);

    void registerReferExpression(GuluReferableExpression expression);


}
