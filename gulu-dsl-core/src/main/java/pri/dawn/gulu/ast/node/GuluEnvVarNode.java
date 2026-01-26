package pri.dawn.gulu.ast.node;

import lombok.Getter;
import pri.dawn.gulu.ast.GuluAstNode;
import pri.dawn.gulu.ast.GuluNodeVisitor;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/01/24/21:57
 */
@Getter
public class GuluEnvVarNode implements GuluAstNode {

    private final String envVarPath;

    public GuluEnvVarNode(String tokenValue) {
        this.envVarPath = tokenValue.substring(1);
    }


    @Override
    public <T> T visit(GuluNodeVisitor<T> visitor) {
        return visitor.visitEnvVarNode(this);
    }
}
