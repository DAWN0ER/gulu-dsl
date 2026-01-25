package pri.dawn.gulu.extension;

import pri.dawn.gulu.ast.GuluNodeVisitor;
import pri.dawn.gulu.ast.node.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/01/25/14:03
 */
public class DescriptionVisitor implements GuluNodeVisitor<String> {

    private String getFormattedDescription(Class<?> nodeClass, Object... description) {
        StringBuilder sb = new StringBuilder();
        sb.append(nodeClass.getSimpleName());
        if (description.length > 0) {
            sb.append('-');
        }
        for (Object desc : description) {
            sb.append("[ ");
            sb.append(desc);
            sb.append(" ]");
        }
        return sb.toString();
    }

    @Override
    public String visitBoolLogicNode(GuluBoolLogicNode node) {
        return getFormattedDescription(GuluBoolLogicNode.class, node.getOp());
    }

    @Override
    public String visitBoolNotNode(GuluBoolNotNode node) {
        return getFormattedDescription(GuluBoolNotNode.class);
    }

    @Override
    public String visitBinaryCompareNode(GuluBinaryCompareNode node) {
        return getFormattedDescription(GuluBinaryCompareNode.class, node.getCompareOp());
    }

    @Override
    public String visitIdentifierNode(GuluIdentifierNode node) {
        return getFormattedDescription(GuluIdentifierNode.class, node.getPath());
    }

    @Override
    public String visitExistedNode(GuluExistedNode node) {
        return getFormattedDescription(GuluExistedNode.class);
    }

    @Override
    public String visitInFuncNode(GuluInFuncNode node) {
        return getFormattedDescription(GuluInFuncNode.class);
    }

    @Override
    public String visitContainsNode(GuluContainsNode node) {
        return getFormattedDescription(GuluContainsNode.class);
    }

    @Override
    public String visitNestedNode(GuluNestedNode node) {
        return getFormattedDescription(GuluNestedNode.class);
    }

    @Override
    public String visitNumberNode(GuluNumberNode node) {
        return getFormattedDescription(GuluNumberNode.class, node.getType(), node.getValue());
    }

    @Override
    public String visitStringNode(GuluStringNode node) {
        return getFormattedDescription(GuluStringNode.class, node.getValue());
    }

    @Override
    public String visitBooleanNode(GuluBooleanNode node) {
        return getFormattedDescription(GuluBooleanNode.class, node.getValue());
    }

    @Override
    public String visitEnvVarNode(GuluEnvVarNode node) {
        return getFormattedDescription(GuluEnvVarNode.class, node.getEnvIdentifier());
    }

    @Override
    public String visitReferNode(GuluReferNode node) {
        return getFormattedDescription(GuluReferNode.class, node.getReferPath());
    }
}
