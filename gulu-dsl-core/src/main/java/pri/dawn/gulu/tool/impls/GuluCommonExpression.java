package pri.dawn.gulu.tool.impls;

import pri.dawn.gulu.ast.GuluAstNode;
import pri.dawn.gulu.ast.GuluEvalBoolNode;
import pri.dawn.gulu.ast.GuluParser;
import pri.dawn.gulu.exception.ExpressionEvaluateException;
import pri.dawn.gulu.token.GuluLexer;
import pri.dawn.gulu.tool.GuluContext;
import pri.dawn.gulu.tool.GuluExpression;

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
}
