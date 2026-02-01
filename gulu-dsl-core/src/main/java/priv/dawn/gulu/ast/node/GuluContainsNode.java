package priv.dawn.gulu.ast.node;

import lombok.Getter;
import priv.dawn.gulu.ast.GuluAstNode;
import priv.dawn.gulu.ast.GuluEvalBoolNode;
import priv.dawn.gulu.ast.GuluNodeVisitor;
import priv.dawn.gulu.exception.ExpressionEvaluateException;
import priv.dawn.gulu.tool.GuluContext;
import priv.dawn.gulu.utils.GuluNodeValueUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/01/24/20:52
 */
@Getter
public class GuluContainsNode implements GuluEvalBoolNode {

    private final GuluIdentifierNode identifier;
    private final List<GuluAstNode> containsList; // number,string

    public GuluContainsNode(GuluIdentifierNode identifier, List<GuluAstNode> containsList) {
        this.identifier = identifier;
        this.containsList = containsList;
    }

    @Override
    public List<? extends GuluAstNode> getChildren() {
        List<GuluAstNode> children = new ArrayList<>();
        children.add(identifier);
        children.addAll(containsList);
        return children;
    }

    @Override
    public <T> T accent(GuluNodeVisitor<T> visitor) {
        return visitor.visitContainsNode(this);
    }

    @Override
    public boolean evaluate(GuluContext context) {
        Object identifier = context.getIdentifier(this.identifier.getPath());
        if (identifier != null && !(identifier instanceof Collection)) {
            throw new ExpressionEvaluateException("Identifier is not a collection");
        }
        if (identifier == null || ((Collection<?>) identifier).isEmpty()) {
            return false;
        }
        Collection<?> collectionIdentifier = (Collection<?>) identifier;
        Set<Long> millsConvertSet = collectionIdentifier.stream().map(e -> GuluNodeValueUtils.tryConvertEpochMills(e, context.getSupportedDateFormat()))
                .filter(Objects::nonNull).collect(Collectors.toSet());

        for (GuluAstNode node : containsList) {
            Object nodeValue = GuluNodeValueUtils.value(node, context);
            Long nodeMillValue = GuluNodeValueUtils.tryConvertEpochMills(nodeValue, context.getSupportedDateFormat());

            boolean valueMatched = collectionIdentifier.contains(nodeValue);
            boolean timeMatched = nodeMillValue != null && millsConvertSet.contains(nodeMillValue);

            if (!valueMatched && !timeMatched) {
                return false;
            }
        }
        return true;
    }


}
