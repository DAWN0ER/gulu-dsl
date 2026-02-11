package priv.dawn.gulu.ast;

import java.util.List;

/**
 * @author Dawn Yang
 * @since 2026/01/23/22:59
 */
public interface GuluAstNode {

    default List<? extends GuluAstNode> getChildren(){
        return null;
    }

    <T> T accept(GuluNodeVisitor<T> visitor);

}
