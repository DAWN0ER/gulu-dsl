package priv.dawn.gulu.ast.node;

import lombok.Getter;
import priv.dawn.gulu.ast.GuluAstNode;
import priv.dawn.gulu.ast.GuluEvalBoolNode;
import priv.dawn.gulu.ast.GuluNodeVisitor;
import priv.dawn.gulu.exception.ExpressionEvaluateException;
import priv.dawn.gulu.token.GuluToken;
import priv.dawn.gulu.api.GuluContext;
import priv.dawn.gulu.utils.GuluAstNodeComparator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/01/24/17:07
 */
@Getter
public class GuluBinaryCompareNode implements GuluEvalBoolNode {

    private final GuluAstNode left;
    private final GuluAstNode right;
    private final GuluToken.Type compareOp;

    public GuluBinaryCompareNode(GuluAstNode left, GuluToken.Type compareOp, GuluAstNode right) {
        this.left = left;
        this.right = right;
        this.compareOp = compareOp;
    }

    @Override
    public List<? extends GuluAstNode> getChildren() {
        List<GuluAstNode> children = new ArrayList<>();
        children.add(left);
        children.add(right);
        return children;
    }

    @Override
    public <T> T accept(GuluNodeVisitor<T> visitor) {
        return visitor.visitBinaryCompareNode(this);
    }


    @Override
    public boolean evaluate(GuluContext context) {
        GuluAstNodeComparator comparator = new GuluAstNodeComparator(context);
        int compare = comparator.compare(left, right);
        switch (compareOp) {
            case GT:
                return compare > 0;
            case LT:
                return compare < 0;
            case GTE:
                return compare >= 0;
            case LTE:
                return compare <= 0;
            case EQ:
                return compare == 0;
            case NE:
                return compare != 0;
            default:
                throw new ExpressionEvaluateException("Unsupported compare operator");
        }
    }

}
