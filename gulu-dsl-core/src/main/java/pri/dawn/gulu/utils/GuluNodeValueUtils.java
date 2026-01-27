package pri.dawn.gulu.utils;

import pri.dawn.gulu.ast.GuluAstNode;
import pri.dawn.gulu.ast.node.*;
import pri.dawn.gulu.exception.ExpressionEvaluateException;
import pri.dawn.gulu.tool.GuluContext;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/01/27/0:44
 */
public class GuluNodeValueUtils {

    // TODO 获取节点在当前上下文的 value 值和对应的转换规则
    public static Object value(GuluAstNode node, GuluContext ctx){
        if (node instanceof GuluIdentifierNode) {
            return ctx.getIdentifier(((GuluIdentifierNode) node).getPath());
        }
        if (node instanceof GuluStringNode) {
            return ((GuluStringNode) node).getValue();
        }
        if (node instanceof GuluBooleanNode){
            return ((GuluBooleanNode) node).getValue();
        }
        if (node instanceof GuluNumberNode) {
            return ((GuluNumberNode) node).getValue();
        }
        if (node instanceof GuluEnvVarNode) {
            return ctx.getEnvVar(((GuluEnvVarNode) node).getEnvVarPath());
        }
        throw new ExpressionEvaluateException("Unsupported AstNode in BinaryCompareNode");
    }

}
