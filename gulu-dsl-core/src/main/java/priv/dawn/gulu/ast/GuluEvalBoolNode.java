package priv.dawn.gulu.ast;

import priv.dawn.gulu.tool.GuluContext;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/01/24/16:34
 */
public interface GuluEvalBoolNode extends GuluAstNode {

    boolean evaluate(GuluContext context);

}
