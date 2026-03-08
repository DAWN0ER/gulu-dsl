package priv.dawn.gulu.api.impls.ctx;

import org.junit.Test;
import priv.dawn.gulu.anotions.GuluCtxPath;
import priv.dawn.gulu.exception.ExpressionEvaluateException;

import static org.junit.Assert.*;

/**
 * GuluGetterContext 单元测试
 * 
 * @author Dawn Yang
 * @since 2026/03/08/22:05
 */
public class GuluGetterContextTest {

    /**
     * 测试用的数据类
     */
    public static class TestData {
        private String name = "test";
        private Integer age = 25;
        private NestedData nested = new NestedData();

        @GuluCtxPath("name")
        public String getName() {
            return name;
        }

        @GuluCtxPath("age")
        public Integer getAge() {
            return age;
        }

        @GuluCtxPath("nested")
        public NestedData getNested() {
            return nested;
        }

        // 没有注解的方法，不应该被查找
        public String getEmail() {
            return "test@example.com";
        }

        public void setNested(NestedData nested) {
            this.nested = nested;
        }
    }

    /**
     * 嵌套测试数据
     */
    public static class NestedData {
        private String value = "nestedValue";

        @GuluCtxPath("value")
        public String getValue() {
            return value;
        }
    }

    /**
     * 测试：获取简单属性值
     */
    @Test
    public void testGetSimpleProperty() throws Exception {
        TestData data = new TestData();
        GuluGetterContext context = new GuluGetterContext(data);

        Object name = context.getIdentifier("name");
        assertEquals("test", name);

        Object age = context.getIdentifier("age");
        assertEquals(Integer.valueOf(25), age);
    }

    /**
     * 测试：获取嵌套对象属性
     */
    @Test
    public void testGetNestedProperty() throws Exception {
        TestData data = new TestData();
        GuluGetterContext context = new GuluGetterContext(data);

        Object nested = context.getIdentifier("nested");
        assertNotNull(nested);
        assertTrue(nested instanceof NestedData);
    }

    /**
     * 测试：级联路径访问
     */
    @Test
    public void testCascadePath() throws Exception {
        TestData data = new TestData();
        GuluGetterContext context = new GuluGetterContext(data);

        // 访问 nested.value，先调用 getNested()，再调用 getValue()
        Object nestedValue = context.getIdentifier("nested.value");
        assertNotNull(nestedValue);
        assertEquals("nestedValue", nestedValue);
    }

    /**
     * 测试：不存在的属性路径
     */
    @Test(expected = ExpressionEvaluateException.class)
    public void testNonExistentPath() {
        TestData data = new TestData();
        GuluGetterContext context = new GuluGetterContext(data);
        context.getIdentifier("nonExistent");
    }

    /**
     * 测试：中间路径为 null
     */
    @Test(expected = RuntimeException.class)
    public void testIntermediatePathIsNull() {
        TestData data = new TestData();
        data.setNested(null);
        GuluGetterContext context = new GuluGetterContext(data);
        
        // 尝试访问 nested.value，但 nested 为 null
        context.getIdentifier("nested.value");
    }

    /**
     * 测试：缓存机制 - 同一个类的多个实例应该共享缓存
     */
    @Test
    public void testCacheMechanism() throws Exception {
        TestData data1 = new TestData();
        TestData data2 = new TestData();
        
        GuluGetterContext context1 = new GuluGetterContext(data1);
        GuluGetterContext context2 = new GuluGetterContext(data2);
        
        // 第一次调用
        context1.getIdentifier("name");
        // 第二次调用应该使用缓存
        context2.getIdentifier("name");
        
        // 验证结果正确性
        assertEquals("test", context1.getIdentifier("name"));
        assertEquals("test", context2.getIdentifier("name"));
    }

    /**
     * 测试：只有带 @GuluCtxPath 注解的方法才能被访问
     */
    @Test(expected = ExpressionEvaluateException.class)
    public void testOnlyAnnotatedMethodsAccessible() {
        TestData data = new TestData();
        GuluGetterContext context = new GuluGetterContext(data);
        
        // email 方法没有 @GuluCtxPath 注解，应该找不到
        context.getIdentifier("email");
    }

    /**
     * 测试：不同数据类型的返回值
     */
    @Test
    public void testDifferentReturnTypes() throws Exception {
        TestData data = new TestData();
        GuluGetterContext context = new GuluGetterContext(data);

        // String 类型
        assertEquals("test", context.getIdentifier("name"));
        
        // Integer 类型
        assertEquals(Integer.valueOf(25), context.getIdentifier("age"));
        
        // 对象类型
        assertNotNull(context.getIdentifier("nested"));
    }

    /**
     * 测试：构造方法传入 null 对象
     */
    @Test
    public void testConstructorWithNull() {
        GuluGetterContext context = new GuluGetterContext(null);
        assertNotNull(context);
    }

    /**
     * 测试：运行时抛出异常的处理
     */
    public static class TestWithException {
        @GuluCtxPath("error")
        public String throwError() {
            throw new RuntimeException("Intentional error");
        }
    }

    @Test(expected = RuntimeException.class)
    public void testMethodThrowsException() {
        TestWithException data = new TestWithException();
        GuluGetterContext context = new GuluGetterContext(data);
        context.getIdentifier("error");
    }

    /**
     * 测试：多级深度路径
     */
    public static class Level3Data {
        private String finalValue = "level3";

        @GuluCtxPath("finalValue")
        public String getFinalValue() {
            return finalValue;
        }
    }

    public static class Level2Data {
        private Level3Data level3 = new Level3Data();

        @GuluCtxPath("level3")
        public Level3Data getLevel3() {
            return level3;
        }
    }

    public static class Level1Data {
        private Level2Data level2 = new Level2Data();

        @GuluCtxPath("level2")
        public Level2Data getLevel2() {
            return level2;
        }
    }

    @Test
    public void testMultiLevelPath() throws Exception {
        Level1Data data = new Level1Data();
        GuluGetterContext context = new GuluGetterContext(data);

        // 访问 level2.level3.finalValue
        Object result = context.getIdentifier("level2.level3.finalValue");
        assertNotNull(result);
        assertEquals("level3", result);
    }

    /**
     * 测试：空字符串路径
     */
    @Test
    public void testEmptyPath() {
        TestData data = new TestData();
        GuluGetterContext context = new GuluGetterContext(data);
        
        try {
            context.getIdentifier("");
            fail("Expected exception for empty path");
        } catch (Exception e) {
            // 预期会抛出异常
            assertTrue(e instanceof RuntimeException || e instanceof ArrayIndexOutOfBoundsException);
        }
    }

    /**
     * 测试：重复调用同一路径（验证缓存效果）
     */
    @Test
    public void testRepeatedCalls() throws Exception {
        TestData data = new TestData();
        GuluGetterContext context = new GuluGetterContext(data);

        // 多次调用同一路径
        for (int i = 0; i < 100; i++) {
            Object result = context.getIdentifier("name");
            assertEquals("test", result);
        }
    }
}
