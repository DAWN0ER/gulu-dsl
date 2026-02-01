package priv.dawn.gulu.tool;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import priv.dawn.gulu.tool.impls.GuluReflectionContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/01/27/23:30
 */
@Slf4j
public class ExpressionTest {

    @Test
    public void testEval() {
        String expressionText = "age > 18 AND name == 'bob'";
        GuluExpression expression = GuluExpressions.parser(expressionText);
        User user = new User();
        user.setAge(40);
        user.setName("bob");
        GuluReflectionContext context = new GuluReflectionContext(user);
        Assert.assertTrue(expression.evaluate(context));
    }

    @Test
    public void testDateList() throws ParseException {
        User user = new User();
        List<Date> list = new ArrayList<>();
        list.add(new Date(System.currentTimeMillis()));
        list.add(new Date(1696673600000L));
        list.add(new Date(1696474600000L));
        list.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-09-19 10:10:00"));
        user.setTimes(list);
        GuluReflectionContext context = new GuluReflectionContext(user);
        String text = "times [ 1696673600000L,'2014-09-19 10:10:00' ]";
        boolean evaluate = GuluExpressions.parser(text).evaluate(context);
        Assert.assertTrue(evaluate);
    }


    @Data
    public static class User{
        private Integer age;
        private String name;
        private List<Date> times;
    }

}
