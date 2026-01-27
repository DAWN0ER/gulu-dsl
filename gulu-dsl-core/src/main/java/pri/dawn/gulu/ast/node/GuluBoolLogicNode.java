package pri.dawn.gulu.ast.node;

import lombok.Getter;
import pri.dawn.gulu.ast.GuluAstNode;
import pri.dawn.gulu.ast.GuluEvalBoolNode;
import pri.dawn.gulu.ast.GuluNodeVisitor;
import pri.dawn.gulu.exception.ExpressionEvaluateException;
import pri.dawn.gulu.token.GuluToken;
import pri.dawn.gulu.tool.GuluContext;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/01/24/18:45
 */
@Getter
public class GuluBoolLogicNode implements GuluEvalBoolNode {

    private final List<GuluEvalBoolNode> conditions;
    private final GuluToken.Type op;

    private GuluBoolLogicNode(List<GuluEvalBoolNode> conditions, GuluToken.Type op) {
        this.conditions = conditions;
        this.op = op;
    }

    public static GuluBoolLogicNode and(List<GuluEvalBoolNode> conditions){
        return new GuluBoolLogicNode(conditions, GuluToken.Type.AND);
    }

    public static GuluBoolLogicNode or(List<GuluEvalBoolNode> conditions){
        return new GuluBoolLogicNode(conditions, GuluToken.Type.OR);
    }

    @Override
    public List<? extends GuluAstNode> getChildren() {
        return conditions;
    }

    @Override
    public <T> T accent(GuluNodeVisitor<T> visitor) {
        return visitor.visitBoolLogicNode(this);
    }

    @Override
    public boolean evaluate(GuluContext context) {
        if(op == GuluToken.Type.AND){
            for (GuluEvalBoolNode condition : conditions) {
                if(!condition.evaluate(context)){
                    return false;
                }
            }
            return true;
        }
        if(op == GuluToken.Type.OR){
            for (GuluEvalBoolNode condition : conditions) {
                if(condition.evaluate(context)){
                    return true;
                }
            }
            return false;
        }
        throw new ExpressionEvaluateException("Illegal logical operator: must be AND/OR.");
    }

}
