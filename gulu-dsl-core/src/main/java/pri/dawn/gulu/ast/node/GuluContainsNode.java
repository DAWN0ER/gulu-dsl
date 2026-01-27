package pri.dawn.gulu.ast.node;

import lombok.Getter;
import pri.dawn.gulu.ast.GuluAstNode;
import pri.dawn.gulu.ast.GuluEvalBoolNode;
import pri.dawn.gulu.ast.GuluNodeVisitor;
import pri.dawn.gulu.exception.ExpressionEvaluateException;
import pri.dawn.gulu.tool.GuluContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/01/24/20:52
 */
@Getter
public class GuluContainsNode implements GuluEvalBoolNode {

    private final GuluIdentifierNode identifier;
    private final List<GuluAstNode> containsList; // number,string

    public GuluContainsNode(GuluIdentifierNode identifier, List<GuluAstNode> containsList) {
        this.identifier = identifier;
        this.containsList = containsList;
    }

    @Override
    public List<? extends GuluAstNode> getChildren() {
        List<GuluAstNode> children = new ArrayList<>();
        children.add(identifier);
        children.addAll(containsList);
        return children;
    }

    @Override
    public <T> T accent(GuluNodeVisitor<T> visitor) {
        return visitor.visitContainsNode(this);
    }

    @Override
    public boolean evaluate(GuluContext context) {
        Object identifier = context.getIdentifier(this.identifier.getPath());
        if(!(identifier instanceof Collection)){
            throw new ExpressionEvaluateException("Identifier is not a collection");
        }
        Collection<?> collectionIdentifier = (Collection<?>) identifier;
        for (GuluAstNode node : containsList) {
            if(!collectionIdentifier.contains(getValue(node,context))){
                return false;
            }
        }
        return true;
    }

    private Object getValue(GuluAstNode node, GuluContext ctx) {
        if (node instanceof GuluStringNode) {
            return ((GuluStringNode) node).getValue();
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
