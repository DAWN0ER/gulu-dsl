package pri.dawn.gulu.ast.node;

import lombok.Getter;
import pri.dawn.gulu.ast.GuluAstNode;
import pri.dawn.gulu.ast.GuluEvalBoolNode;
import pri.dawn.gulu.ast.GuluNodeVisitor;
import pri.dawn.gulu.exception.ExpressionEvaluateException;
import pri.dawn.gulu.token.GuluToken;
import pri.dawn.gulu.tool.GuluContext;
import pri.dawn.gulu.utils.GuluAstNodeComparator;

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
    public <T> T accent(GuluNodeVisitor<T> visitor) {
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
