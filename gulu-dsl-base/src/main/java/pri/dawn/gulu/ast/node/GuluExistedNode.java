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
 * @since 2026/01/24/20:47
 */
@Getter
public class GuluExistedNode implements GuluEvalBoolNode {

    private final GuluIdentifierNode identifierNode;

    public GuluExistedNode(GuluIdentifierNode identifierNode) {
        this.identifierNode = identifierNode;
    }

    @Override
    public List<GuluAstNode> getChildren() {
        List<GuluAstNode> children = new ArrayList<>();
        children.add(identifierNode);
        return children;
    }

    @Override
    public <T> T visit(GuluNodeVisitor<T> visitor) {
        return visitor.visitExistedNode(this);
    }


    @Override
    public boolean evaluate(GuluContext context) {
        return context.getIdentifier(identifierNode.getPath()) != null;
    }
}
