package pri.dawn.gulu.ast.node;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pri.dawn.gulu.ast.GuluAstNode;
import pri.dawn.gulu.ast.GuluNodeVisitor;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/01/23/23:27
 */
@Getter
@AllArgsConstructor
public class GuluIdentifierNode implements GuluAstNode {

    @Getter
    private String path;

    @Override
    public <T> T visit(GuluNodeVisitor<T> visitor) {
        return visitor.visitIdentifierNode(this);
    }
}
