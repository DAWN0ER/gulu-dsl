package priv.dawn.gulu.api;

import org.junit.Assert;
import org.junit.Test;
import priv.dawn.gulu.exception.ExpressionEvaluateException;
import priv.dawn.gulu.api.impls.ctx.GuluReflectionContext;
import priv.dawn.gulu.utils.pojo.BaseData;

/**
 * @author Dawn Yang
 * @since 2026/01/28/21:26
 */
public class ReferExpressionTest {

    @Test
    public void testRefer() {
        BaseData data = new BaseData();
        data.setAInt(10);
        data.setALong(20L);
        GuluReflectionContext context = new GuluReflectionContext(data);
        GuluExpressions.parserAndRegister("aInt>1", "t1", context);
        GuluExpressions.parserAndRegister("aLong>10", "t2", context);
        GuluExpression parser = GuluExpressions.parser("#{t1} and #{t2}");
        Assert.assertTrue(parser.evaluate(context));
    }

    @Test
    public void testCircleRefer() {
        BaseData data = new BaseData();
        data.setAInt(10);
        data.setALong(20L);
        GuluReflectionContext context = new GuluReflectionContext(data);
        GuluExpressions.parserAndRegister("aInt>1 AND #{t2}", "t1", context);
        GuluExpressions.parserAndRegister("aLong>10 and #{t1}" , "t2", context);
        GuluExpression parser = GuluExpressions.parser("#{t1} and #{t2}");
        Assert.assertThrows(ExpressionEvaluateException.class,()-> parser.evaluate(context));
    }

}
