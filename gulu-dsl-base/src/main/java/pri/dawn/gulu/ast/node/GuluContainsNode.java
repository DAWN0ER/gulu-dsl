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
    public List<GuluAstNode> getChildren() {
        List<GuluAstNode> children = new ArrayList<>();
        children.add(identifier);
        children.addAll(containsList);
        return children;
    }

    @Override
    public <T> T visit(GuluNodeVisitor<T> visitor) {
        return visitor.visitContainsNode(this);
    }

    @Override
    public boolean evaluate(GuluContext context) {
        return false;
    }
}
