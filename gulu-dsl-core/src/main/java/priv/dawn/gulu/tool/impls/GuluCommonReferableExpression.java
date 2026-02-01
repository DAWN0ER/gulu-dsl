package priv.dawn.gulu.tool.impls;

import priv.dawn.gulu.ast.GuluAstNode;
import priv.dawn.gulu.ast.GuluEvalBoolNode;
import priv.dawn.gulu.ast.GuluParser;
import priv.dawn.gulu.exception.ExpressionEvaluateException;
import priv.dawn.gulu.token.GuluLexer;
import priv.dawn.gulu.tool.GuluContext;
import priv.dawn.gulu.tool.GuluReferableExpression;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/01/25/13:08
 */
public class GuluCommonReferableExpression implements GuluReferableExpression {

    private final String expressionText;
    private final GuluAstNode root;
    private final String referPath;

    public GuluCommonReferableExpression(String expressionText, String referPath) {
        this.expressionText = expressionText;
        this.referPath = referPath;
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
    public String getReferPath() {
        return referPath;
    }

    @Override
    public GuluAstNode getAstRootNode() {
        return root;
    }
}
