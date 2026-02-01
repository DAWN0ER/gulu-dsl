package priv.dawn.gulu.tool;

import org.junit.Assert;
import org.junit.Test;
import priv.dawn.gulu.tool.impls.GuluReflectionContext;
import priv.dawn.gulu.utils.pojo.BaseData;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/02/01/20:15
 */
public class NestedExpressionTest {

    @Test
    public void testNested(){
        BaseData baseData = new BaseData();
        BaseData sub1 = new BaseData();
        BaseData sub2 = new BaseData();
        baseData.setASubObjList(new ArrayList<>());
        baseData.getASubObjList().add(sub1);
        baseData.getASubObjList().add(sub2);
        sub1.setAInt(12);
        GuluExpression parser = GuluExpressions.parser("aSubObjList[aInt > 0]");
        boolean evaluate = parser.evaluate(new GuluReflectionContext(baseData));
        Assert.assertTrue(evaluate);
    }

    @Test
    public void testNested2(){
        BaseData baseData = new BaseData();
        BaseData sub1 = new BaseData();
        BaseData sub2 = new BaseData();
        baseData.setASubObjList(new ArrayList<>());
        baseData.getASubObjList().add(sub1);
        baseData.getASubObjList().add(sub2);
        sub2.setAIntList(Arrays.asList(1,2,3));
        GuluExpression parser = GuluExpressions.parser("aSubObjList[aIntList[1,2,3]]");
        boolean evaluate = parser.evaluate(new GuluReflectionContext(baseData));
        Assert.assertTrue(evaluate);
    }

}
