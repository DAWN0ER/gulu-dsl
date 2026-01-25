package pri.dawn.gulu.tool;

import pri.dawn.gulu.ast.GuluAstNode;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/01/23/20:58
 */
public interface GuluExpression {

    String getExpressionText();

    boolean evaluate(GuluContext context);

    GuluAstNode getAstRootNode();

}
