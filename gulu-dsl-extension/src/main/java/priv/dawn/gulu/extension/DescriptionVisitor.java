package priv.dawn.gulu.extension;

import priv.dawn.gulu.ast.GuluAstNode;
import priv.dawn.gulu.ast.GuluNodeVisitor;
import priv.dawn.gulu.ast.node.*;

import java.util.List;

/**
 * @author Dawn Yang
 * @since 2026/01/25/14:03
 */
public class DescriptionVisitor implements GuluNodeVisitor<String> {

    public String ast2StringDescription(GuluAstNode root) {
        if (root == null) {
            return "";
        }
        StringBuilder res = new StringBuilder(root.accept(this) + "\n");
        List<? extends GuluAstNode> children = root.getChildren();
        if (children != null) {
            for (int i = 0; i < children.size(); i++) {
                boolean last = (i == children.size() - 1);
                res.append(tree2StringCore(children.get(i), "    ", last));
            }
        }
        return res.toString();
    }

    private StringBuilder tree2StringCore(GuluAstNode node, String prefix, boolean isLast) {
        if (node == null) return new StringBuilder();

        StringBuilder res = new StringBuilder(prefix);
        res.append(isLast ? "└── " : "├── ");
        res.append(node.accept(this));
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
        return getFormattedDescription(GuluEnvVarNode.class, node.getEnvVarPath());
    }

    @Override
    public String visitReferNode(GuluReferNode node) {
        return getFormattedDescription(GuluReferNode.class, node.getReferPath());
    }
}
