package pri.dawn.gulu.ast.node;

import lombok.Getter;
import pri.dawn.gulu.ast.GuluEvalBoolNode;
import pri.dawn.gulu.ast.GuluNodeVisitor;
import pri.dawn.gulu.exception.ExpressionEvaluateException;
import pri.dawn.gulu.tool.GuluContext;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/01/24/22:10
 */
@Getter
public class GuluReferNode implements GuluEvalBoolNode {

    private final String referPath;

    public GuluReferNode(String tokenValue) {
        // #{path.path} -> path.path
        this.referPath = tokenValue.substring(2, tokenValue.length() - 1);
    }

    @Override
    public boolean evaluate(GuluContext context) {
        Boolean result = context.getReferExpressionResult(referPath);
        if (result == null) {
            throw new ExpressionEvaluateException("Refer expression can not evaluate to boolean");
        }
        return result;
    }

    @Override
    public <T> T accent(GuluNodeVisitor<T> visitor) {
        return visitor.visitReferNode(this);
    }
}
