package priv.dawn.gulu.ast.node;

import lombok.Getter;
import priv.dawn.gulu.ast.GuluAstNode;
import priv.dawn.gulu.ast.GuluNodeVisitor;

/**
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
    public <T> T accept(GuluNodeVisitor<T> visitor) {
        return visitor.visitEnvVarNode(this);
    }
}
