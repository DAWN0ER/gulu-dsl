package priv.dawn.gulu.tool.impls;

import priv.dawn.gulu.ast.GuluAstNode;
import priv.dawn.gulu.ast.GuluEvalBoolNode;
import priv.dawn.gulu.ast.GuluParser;
import priv.dawn.gulu.exception.ExpressionEvaluateException;
import priv.dawn.gulu.token.GuluLexer;
import priv.dawn.gulu.tool.GuluContext;
import priv.dawn.gulu.tool.GuluExpression;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/01/25/13:07
 */
public class GuluCommonExpression implements GuluExpression {

   private final String expressionText;
   private final GuluAstNode root;

    public GuluCommonExpression(String expressionText) {
        this.expressionText = expressionText;
        this.root = new GuluParser(new GuluLexer(expressionText).tokenize()).parser();
    }

    @Override
    public String getExpressionText() {
        return expressionText;
    }

    @Override
    public boolean evaluate(GuluContext context) {
        if(root instanceof GuluEvalBoolNode){
            return ((GuluEvalBoolNode) root).evaluate(context);
        }
        throw new ExpressionEvaluateException("Expression can not evaluate to boolean");
    }

    @Override
    public GuluAstNode getAstRootNode() {
        return root;
    }
}
