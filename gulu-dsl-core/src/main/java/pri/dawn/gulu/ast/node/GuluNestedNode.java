package pri.dawn.gulu.ast.node;

import lombok.Getter;
import pri.dawn.gulu.ast.GuluAstNode;
import pri.dawn.gulu.ast.GuluEvalBoolNode;
import pri.dawn.gulu.ast.GuluNodeVisitor;
import pri.dawn.gulu.ast.NestedSubContext;
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
 * @since 2026/01/24/20:57
 */
@Getter
public class GuluNestedNode implements GuluEvalBoolNode {

    private final GuluIdentifierNode pathIdentifier;
    private final GuluAstNode nestedExpression;

    public GuluNestedNode(GuluIdentifierNode pathIdentifier, GuluAstNode nestedExpression) {
        this.pathIdentifier = pathIdentifier;
        this.nestedExpression = nestedExpression;
    }

    @Override
    public List<? extends GuluAstNode> getChildren() {
        List<GuluAstNode> children = new ArrayList<>();
        children.add(pathIdentifier);
        children.add(nestedExpression);
        return children;
    }

    @Override
    public <T> T accent(GuluNodeVisitor<T> visitor) {
        return visitor.visitNestedNode(this);
    }

    @Override
    public boolean evaluate(GuluContext context) {
        Object identifier = context.getIdentifier(this.pathIdentifier.getPath());
        if (!(identifier instanceof Collection)) {
            throw new ExpressionEvaluateException(String.format("`%s` is not a collection!", this.pathIdentifier.getPath()));
        }
        if (!(nestedExpression instanceof GuluEvalBoolNode)) {
            throw new ExpressionEvaluateException("Nested expression is not a boolean expression!");
        }
        @SuppressWarnings("unchecked")
        Collection<Object> nestedObjects = (Collection<Object>) identifier;
        for (Object nestedObject : nestedObjects) {
            NestedSubContext subContext = new NestedSubContext(context, nestedObject);
            boolean evaluate = ((GuluEvalBoolNode) nestedExpression).evaluate(subContext);
            if (evaluate) {
                return true;
            }
        }
        return false;
    }
}
