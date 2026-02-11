package priv.dawn.gulu.api.impls.ctx;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * @author Dawn Yang
 * @since 2026/02/11/22:45
 */
public class BaseGuluContextTest {

    @Test
    public void mapContextTest() {
        GuluMapContext context = new GuluMapContext();
        String val = "default_test_value";
        String path = "a.b.c.e";
        context.put(path, val);
        Assert.assertEquals(context.getIdentifier(path), val);
        Object a = context.getIdentifier("a");
        Assert.assertTrue(a instanceof Map);
        Object b = ((Map<?, ?>) a).get("b");
        Assert.assertTrue(b instanceof Map);
        Object c = ((Map<?, ?>) b).get("c");
        Assert.assertTrue(c instanceof Map);
        Assert.assertEquals(((Map<?, ?>) c).get("e"),val);
    }

    @Test
    public void multiMapPutTest(){
        GuluMapContext context = new GuluMapContext();
        context.put("a.b.c.d", "1");
        context.put("a.b.c.e", "2");
        context.put("a.b.c.f", "3");
        Assert.assertEquals(context.getIdentifier("a.b.c.d"), "1");
        Assert.assertEquals(context.getIdentifier("a.b.c.e"), "2");
        Assert.assertEquals(context.getIdentifier("a.b.c.f"), "3");
        Object abc = context.getIdentifier("a.b.c");
        Assert.assertTrue(abc instanceof Map);
        Assert.assertEquals(((Map<?, ?>) abc).size(),3);
    }

}