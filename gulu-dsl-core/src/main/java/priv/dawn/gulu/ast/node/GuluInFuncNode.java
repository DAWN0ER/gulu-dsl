package priv.dawn.gulu.ast.node;

import lombok.Getter;
import priv.dawn.gulu.ast.GuluAstNode;
import priv.dawn.gulu.ast.GuluEvalBoolNode;
import priv.dawn.gulu.ast.GuluNodeVisitor;
import priv.dawn.gulu.tool.GuluContext;
import priv.dawn.gulu.utils.GuluAstNodeComparator;

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
        // if identifier value equals any value in literalList, return true
        GuluAstNodeComparator comparator = new GuluAstNodeComparator(context);
        for (GuluAstNode node : literalList) {
            if (comparator.compare(node, identifier) == 0) {
                return true;
            }
        }
        return false;
    }
}
