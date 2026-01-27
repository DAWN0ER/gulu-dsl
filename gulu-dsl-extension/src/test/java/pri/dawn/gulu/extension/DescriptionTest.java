package pri.dawn.gulu.extension;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import pri.dawn.gulu.ast.GuluAstNode;
import pri.dawn.gulu.tool.GuluExpression;
import pri.dawn.gulu.tool.GuluExpressions;

import java.util.List;

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
        String desc = tree2String(expression.getAstRootNode());
        log.debug("description of ast node is:\n{}", desc);
    }



    private static String tree2String(GuluAstNode root) {
        if (root == null) {
            return "";
        }
        StringBuilder res = new StringBuilder(root.accent(new DescriptionVisitor()) + "\n");
        List<? extends GuluAstNode> children = root.getChildren();
        if (children != null) {
            for (int i = 0; i < children.size(); i++) {
                boolean last = (i == children.size() - 1);
                res.append(tree2StringCore(children.get(i), "    ", last));
            }
        }
        return res.toString();
    }

    private static StringBuilder tree2StringCore(GuluAstNode node, String prefix, boolean isLast) {
        if (node == null) return new StringBuilder();

        StringBuilder res = new StringBuilder(prefix);
        res.append(isLast ? "└── " : "├── ");
        res.append(node.accent(new DescriptionVisitor()));
        res.append('\n');

        List<? extends GuluAstNode> children = node.getChildren();
        if (children != null) {
            for (int i = 0; i < children.size(); i++) {
                boolean last = (i == children.size() - 1);
                res.append(tree2StringCore(children.get(i), prefix + (isLast ? "    " : "│   "), last));
            }
        }
        return res;
    }

}
