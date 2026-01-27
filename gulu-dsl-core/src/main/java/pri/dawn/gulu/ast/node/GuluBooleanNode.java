package pri.dawn.gulu.ast.node;

import lombok.Getter;
import pri.dawn.gulu.ast.GuluEvalBoolNode;
import pri.dawn.gulu.ast.GuluNodeVisitor;
import pri.dawn.gulu.tool.GuluContext;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/01/24/17:03
 */
@Getter
public class GuluBooleanNode implements GuluEvalBoolNode {

    private final Boolean value;

    public GuluBooleanNode(String valStr) {
        this.value = Boolean.valueOf(valStr.toLowerCase());
    }

    @Override
    public <T> T accent(GuluNodeVisitor<T> visitor) {
        return visitor.visitBooleanNode(this);
    }

    @Override
    public boolean evaluate(GuluContext context) {
        return value;
    }
}
