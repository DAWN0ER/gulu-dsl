package pri.dawn.gulu.ast.node;

import lombok.Getter;
import pri.dawn.gulu.ast.GuluAstNode;
import pri.dawn.gulu.ast.GuluEvalBoolNode;
import pri.dawn.gulu.ast.GuluNodeVisitor;
import pri.dawn.gulu.exception.ExpressionEvaluateException;
import pri.dawn.gulu.token.GuluToken;
import pri.dawn.gulu.tool.GuluContext;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
        Object leftValue = getValue(left, context);
        Object rightValue = getValue(right, context);
        if (leftValue == null || rightValue == null) {
            throw new ExpressionEvaluateException("Can not compare null value");
        }
        leftValue = convertFormatStringIfDate(leftValue);
        rightValue = convertFormatStringIfDate(rightValue);
        if (leftValue instanceof Number && rightValue instanceof Number) {
            return compareResult((Number) leftValue, (Number) rightValue, compareOp);
        }
        if (leftValue instanceof String && rightValue instanceof String) {
            return compareResult((String) leftValue, (String) rightValue, compareOp);
        }
        if (leftValue instanceof Boolean && rightValue instanceof Boolean) {
            if (compareOp == GuluToken.Type.EQ) {
                return leftValue == rightValue;
            }
            if (compareOp == GuluToken.Type.NE) {
                return leftValue != rightValue;
            }
            throw new ExpressionEvaluateException("BinaryCompareNode only support EQ/NE when compare Boolean");
        }
        throw new ExpressionEvaluateException("BinaryCompareNode can't compare two value of different value");
    }

    private Object getValue(GuluAstNode node, GuluContext ctx) {
        if (node instanceof GuluIdentifierNode) {
            return ctx.getIdentifier(((GuluIdentifierNode) node).getPath());
        }
        if (node instanceof GuluStringNode) {
            return ((GuluStringNode) node).getValue();
        }
        if (node instanceof GuluBooleanNode){
            return ((GuluBooleanNode) node).getValue();
        }
        if (node instanceof GuluNumberNode) {
            return ((GuluNumberNode) node).getValue();
        }
        if (node instanceof GuluEnvVarNode) {
            return ctx.getEnvVar(((GuluEnvVarNode) node).getEnvVarPath());
        }
        throw new ExpressionEvaluateException("Unsupported AstNode in BinaryCompareNode");
    }

    private boolean compareResult(String left, String right, GuluToken.Type compareOp) {
        switch (compareOp) {
            case EQ:
                return left.compareTo(right) == 0;
            case NE:
                return left.compareTo(right) != 0;
            case GT:
                return left.compareTo(right) > 0;
            case LT:
                return left.compareTo(right) < 0;
            case GTE:
                return left.compareTo(right) >= 0;
            case LTE:
                return left.compareTo(right) <= 0;
        }
        throw new ExpressionEvaluateException("Unsupported compare operator in BinaryCompareNode");
    }

    private boolean compareResult(Number left, Number right, GuluToken.Type compareOp) {
        switch (compareOp) {
            case EQ:
                return left.doubleValue() == right.doubleValue();
            case NE:
                return left.doubleValue() != right.doubleValue();
            case GT:
                return left.doubleValue() > right.doubleValue();
            case LT:
                return left.doubleValue() < right.doubleValue();
            case GTE:
                return left.doubleValue() >= right.doubleValue();
            case LTE:
                return left.doubleValue() <= right.doubleValue();
        }
        throw new ExpressionEvaluateException("Unsupported compare operator in BinaryCompareNode");
    }

    // format: date ==> yyyy-MM-dd dd:mm:ss
    private Object convertFormatStringIfDate(Object value) {
        String format = "yyyy-MM-dd HH:mm:ss";
        if (value instanceof Date) {
            return new SimpleDateFormat(format).format((Date) value);
        }
        if (value instanceof LocalDateTime) {
            return ((LocalDateTime) value).format(java.time.format.DateTimeFormatter.ofPattern(format));
        }
        if (value instanceof LocalDate) {
            return ((LocalDate) value).format(java.time.format.DateTimeFormatter.ofPattern(format));
        }
        return value;
    }
}
