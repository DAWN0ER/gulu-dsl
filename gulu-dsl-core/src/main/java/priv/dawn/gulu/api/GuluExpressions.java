package priv.dawn.gulu.api;

import priv.dawn.gulu.api.impls.GuluCommonExpression;
import priv.dawn.gulu.api.impls.GuluCommonReferableExpression;
import priv.dawn.gulu.api.impls.ctx.BaseGuluContext;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/01/25/13:08
 */
public class GuluExpressions {

    public static GuluExpression parser(String expression){
        return new GuluCommonExpression(expression);
    }

    public static GuluReferableExpression parserReferable(String expression, String expReferPath){
        return new GuluCommonReferableExpression(expression, expReferPath);
    }

    public static GuluExpression parserAndRegister(String expression, String expReferPath, BaseGuluContext context){
        GuluReferableExpression guluExpression = parserReferable(expression, expReferPath);
        context.registerReferExpression(guluExpression);
        return guluExpression;
    }

}
