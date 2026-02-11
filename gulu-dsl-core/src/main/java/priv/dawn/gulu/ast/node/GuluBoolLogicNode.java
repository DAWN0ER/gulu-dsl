package priv.dawn.gulu.ast.node;

import lombok.Getter;
import priv.dawn.gulu.ast.GuluAstNode;
import priv.dawn.gulu.ast.GuluEvalBoolNode;
import priv.dawn.gulu.ast.GuluNodeVisitor;
import priv.dawn.gulu.exception.ExpressionEvaluateException;
import priv.dawn.gulu.token.GuluToken;
import priv.dawn.gulu.api.GuluContext;

import java.util.List;

/**
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
    public <T> T accept(GuluNodeVisitor<T> visitor) {
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
