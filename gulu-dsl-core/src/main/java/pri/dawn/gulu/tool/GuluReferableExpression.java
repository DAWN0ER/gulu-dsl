package pri.dawn.gulu.tool;

import pri.dawn.gulu.ast.GuluAstNode;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/01/25/12:49
 */
public interface GuluReferableExpression extends GuluExpression {

    String getReferPath();

    GuluAstNode getAstRootNode();

}
