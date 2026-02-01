package priv.dawn.gulu.tool;

import priv.dawn.gulu.ast.GuluAstNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/01/24/16:39
 */
public interface GuluContext {

    Object getIdentifier(String path);

    Object getEnvVar(String path);

    GuluAstNode getReferAstNode(String path);

    Boolean getReferExpressionResult(String path);

    void registerReferExpression(GuluReferableExpression expression);

    default List<String> getSupportedDateFormat(){
        return new ArrayList<>();
    }
}
