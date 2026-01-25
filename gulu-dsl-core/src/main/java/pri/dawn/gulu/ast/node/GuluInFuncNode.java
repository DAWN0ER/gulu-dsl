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
    public <T> T visit(GuluNodeVisitor<T> visitor) {
        return visitor.visitInFuncNode(this);
    }

    @Override
    public boolean evaluate(GuluContext context) {
        return false;
    }
}
