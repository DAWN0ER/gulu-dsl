package priv.dawn.gulu.api;

import priv.dawn.gulu.ast.GuluAstNode;

/**
 * @author Dawn Yang
 * @since 2026/01/23/20:58
 */
public interface GuluExpression {

    /**
     * 获取表达式文本
     * @return 表达式文本
     */
    String getExpressionText();

    /**
     * 获取表达式结果
     * @param context 上下文
     * @return 表达式结果
     */
    boolean evaluate(GuluContext context);

    /**
     * 获取表达式 AST 根节点
     * @return 根节点
     */
    GuluAstNode getAstRootNode();

}
