package pri.dawn.gulu.ast.node;

import lombok.Getter;
import pri.dawn.gulu.ast.GuluAstNode;
import pri.dawn.gulu.ast.GuluEvalBoolNode;
import pri.dawn.gulu.ast.GuluNodeVisitor;
import pri.dawn.gulu.tool.GuluContext;

import java.util.ArrayList;
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
    public List<GuluAstNode> getChildren() {
        List<GuluAstNode> children = new ArrayList<>();
        children.add(pathIdentifier);
        children.add(nestedExpression);
        return children;
    }

    @Override
    public <T> T visit(GuluNodeVisitor<T> visitor) {
        return visitor.visitNestedNode(this);
    }

    @Override
    public boolean evaluate(GuluContext context) {
        return false;
    }
}
