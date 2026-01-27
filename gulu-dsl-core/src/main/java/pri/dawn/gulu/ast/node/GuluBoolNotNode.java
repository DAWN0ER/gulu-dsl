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
 * @since 2026/01/24/20:32
 */
@Getter
public class GuluBoolNotNode implements GuluEvalBoolNode {

    private final GuluEvalBoolNode wrappedNode;

    public GuluBoolNotNode(GuluEvalBoolNode wrappedNode) {
        this.wrappedNode = wrappedNode;
    }

    @Override
    public List<? extends GuluAstNode> getChildren() {
        List<GuluAstNode> children = new ArrayList<>();
        children.add(wrappedNode);
        return children;
    }

    @Override
    public <T> T accent(GuluNodeVisitor<T> visitor) {
        return visitor.visitBoolNotNode(this);
    }

    @Override
    public boolean evaluate(GuluContext context) {
        return !wrappedNode.evaluate(context);
    }
}
