package priv.dawn.gulu.extension;

import org.elasticsearch.index.query.QueryBuilder;
import org.junit.Assert;
import org.junit.Test;
import priv.dawn.gulu.tool.GuluExpression;
import priv.dawn.gulu.tool.GuluExpressions;
import priv.dawn.gulu.tool.impls.GuluReflectionContext;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/02/01/23:32
 */
public class EsQueryTest {

    @Test
    public void test(){
        String expressionText = "NOT (user.base_info.age >= 18 AND (user.score > 95.5f OR user.account.status IN (\"active\",\"verified\"))) OR (EXIST (user.order.id) AND !user.tags ['vip','new'] AND order[order.amount > 1000.0 AND !order.is_refunded == FALSE]) AND (#{user.level} OR NOT order.pay_time < 1672502400)";
        GuluReflectionContext context = new GuluReflectionContext(null);
        GuluExpression expression = GuluExpressions.parser(expressionText);
        GuluExpressions.parserAndRegister("name_refer_key == 'str_str_secret'","user.level",context);
        QueryBuilder accept = expression.getAstRootNode().accept(new EsQueryTransformerVisitor(context));
        Assert.assertNotNull(accept);
    }

    @Test
    public void nestedTest(){
        GuluExpression expression = GuluExpressions.parser("sub[f1 == 2 and f2_sub[ff2>=2 and ff3[1,2,3]] ]");
        GuluReflectionContext context = new GuluReflectionContext(null);
        QueryBuilder accept = expression.getAstRootNode().accept(new EsQueryTransformerVisitor(context));
    }

}
