package priv.dawn.gulu.extension;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import priv.dawn.gulu.api.GuluExpression;
import priv.dawn.gulu.api.GuluExpressions;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/01/25/14:17
 */
@Slf4j
public class DescriptionTest {

    @Test
    public void testDescription() {
        String expressionText = "NOT (user.base_info.age >= 18 AND (user.score > 95.5f OR user.account.status IN (\"active\",\"verified\"))) OR (EXIST (user.order.id) AND !user.tags ['vip','new'] AND order[order.amount > 1000.0 AND !order.is_refunded == FALSE]) AND (#{user.level} OR NOT order.pay_time < 1672502400)";
        GuluExpression expression = GuluExpressions.parser(expressionText);
        String desc = new DescriptionVisitor().ast2StringDescription(expression.getAstRootNode());
        log.debug("description of ast node is:\n{}", desc);
    }
}
