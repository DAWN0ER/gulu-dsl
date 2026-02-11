package priv.dawn.gulu.ast;

import priv.dawn.gulu.api.GuluContext;

/**
 * @author Dawn Yang
 * @since 2026/01/24/16:34
 */
public interface GuluEvalBoolNode extends GuluAstNode {

    boolean evaluate(GuluContext context);

}
