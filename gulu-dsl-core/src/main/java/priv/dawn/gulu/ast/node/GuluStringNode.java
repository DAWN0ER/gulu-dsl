package priv.dawn.gulu.ast.node;

import lombok.AllArgsConstructor;
import lombok.Getter;
import priv.dawn.gulu.ast.GuluAstNode;
import priv.dawn.gulu.ast.GuluNodeVisitor;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/01/24/17:02
 */
@Getter
@AllArgsConstructor
public class GuluStringNode implements GuluAstNode {

    private final String value;

    @Override
    public <T> T accent(GuluNodeVisitor<T> visitor) {
        return visitor.visitStringNode(this);
    }
}
