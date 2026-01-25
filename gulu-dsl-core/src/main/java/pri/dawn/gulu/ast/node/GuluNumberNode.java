package pri.dawn.gulu.ast.node;

import lombok.Getter;
import pri.dawn.gulu.ast.GuluAstNode;
import pri.dawn.gulu.ast.GuluNodeVisitor;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/01/24/16:47
 */
@Getter
public class GuluNumberNode implements GuluAstNode {

    private final NumberType type;
    private final Number value;

    private GuluNumberNode(NumberType type, Number value){
        this.type = type;
        this.value = value;
    }

    public static GuluNumberNode parerValue(String stringVal){
        return null;
    }

    @Override
    public <T> T visit(GuluNodeVisitor<T> visitor) {
        return visitor.visitNumberNode(this);
    }


    public enum NumberType{
        INTEGER,
        LONG,
        DOUBLE,
        FLOAT,
    }
}
