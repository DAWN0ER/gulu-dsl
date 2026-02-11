package priv.dawn.gulu.ast.node;

import lombok.Getter;
import priv.dawn.gulu.ast.GuluAstNode;
import priv.dawn.gulu.ast.GuluEvalBoolNode;
import priv.dawn.gulu.ast.GuluNodeVisitor;
import priv.dawn.gulu.ast.NestedSubContext;
import priv.dawn.gulu.exception.ExpressionEvaluateException;
import priv.dawn.gulu.api.GuluContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
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
    public <T> T accept(GuluNodeVisitor<T> visitor) {
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
