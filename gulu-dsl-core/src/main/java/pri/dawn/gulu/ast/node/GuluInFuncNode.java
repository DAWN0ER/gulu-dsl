package pri.dawn.gulu.ast.node;

import lombok.Getter;
import pri.dawn.gulu.ast.GuluAstNode;
import pri.dawn.gulu.ast.GuluEvalBoolNode;
import pri.dawn.gulu.ast.GuluNodeVisitor;
import pri.dawn.gulu.exception.ExpressionEvaluateException;
import pri.dawn.gulu.tool.GuluContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/01/24/20:36
 */
@Getter
public class GuluInFuncNode implements GuluEvalBoolNode {

    private final GuluIdentifierNode identifier;
    private final List<GuluAstNode> literalList;

    public GuluInFuncNode(GuluIdentifierNode identifier, List<GuluAstNode> literalList) {
        this.identifier = identifier;
        this.literalList = literalList;
    }

    @Override
    public List<? extends GuluAstNode> getChildren() {
        List<GuluAstNode> children = new ArrayList<>();
        children.add(identifier);
        children.addAll(literalList);
        return children;
    }

    @Override
    public <T> T accent(GuluNodeVisitor<T> visitor) {
        return visitor.visitInFuncNode(this);
    }

    @Override
    public boolean evaluate(GuluContext context) {
        Object identifier = context.getIdentifier(this.identifier.getPath());
        // if identifier value equals any value in literalList, return true
        for (GuluAstNode node : literalList) {
            Object value = getValue(node, context);
            if (value.equals(identifier)) {
                return true;
            }
            if(value instanceof Number && identifier instanceof Number){
                if(((Number) value).doubleValue() == ((Number) identifier).doubleValue()){
                    return true;
                }
            }
        }
        return false;
    }

    private Object getValue(GuluAstNode node, GuluContext ctx) {
        if(node instanceof GuluStringNode){
            return ((GuluStringNode) node).getValue();
        }
        if(node instanceof GuluNumberNode){
            return ((GuluNumberNode) node).getValue();
        }
        if(node instanceof GuluBooleanNode){
            return ((GuluBooleanNode) node).getValue();
        }
        if(node instanceof GuluEnvVarNode){
            return ctx.getEnvVar(((GuluEnvVarNode) node).getEnvVarPath());
        }
        throw new ExpressionEvaluateException("Unsupported AstNode in BinaryCompareNode");
    }
}
