package priv.dawn.gulu.api;

import priv.dawn.gulu.ast.GuluAstNode;

/**
 * GuluExpression 执行依赖的上下文
 *
 * @author Dawn Yang
 * @since 2026/01/24/16:39
 */
public interface GuluContext {

    /**
     * 获取标识符的值
     * @param path 标识符路径
     * @return 标识符在当前上下文的值
     */
    Object getIdentifier(String path);

    /**
     * 获取环境变量的值
     * @param path 环境变量路径
     * @return 环境变量在当前上下文的值
     */
    Object getEnvVar(String path);

    /**
     * 获取引用表达式的 AST 根节点
     * @param path 引用表达式路径
     * @return 引用表达式的 AST 根节点
     */
    GuluAstNode getReferAstNode(String path);

    /**
     * 获取引用表达式的计算结果
     * @param path 引用表达式路径
     * @return 引用表达式的计算结果
     */
    Boolean getReferExpressionResult(String path);

    /**
     * 获取当前上下文的配置选项
     * @return 当前上下文配置选项
     */
    GuluContextOptions getOptions();
}
