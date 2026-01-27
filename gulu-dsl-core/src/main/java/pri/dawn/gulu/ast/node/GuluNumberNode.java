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

    private GuluNumberNode(NumberType type, Number value) {
        this.type = type;
        this.value = value;
    }

    public static GuluNumberNode parerValue(String stringVal) {
        if (stringVal.indexOf('d') > 0 || stringVal.indexOf('D') > 0) {
            return new GuluNumberNode(NumberType.DOUBLE, Double.parseDouble(stringVal));
        }
        if (stringVal.indexOf('f') > 0 || stringVal.indexOf('F') > 0) {
            return new GuluNumberNode(NumberType.FLOAT, Float.parseFloat(stringVal));
        }
        if (stringVal.indexOf('l') > 0 || stringVal.indexOf('L') > 0) {
            return new GuluNumberNode(NumberType.LONG, Long.parseLong(stringVal.substring(0,stringVal.length()-1)));
        }
        if (stringVal.indexOf('.') > 0) {
            return new GuluNumberNode(NumberType.DOUBLE, Double.parseDouble(stringVal));
        }
        return new GuluNumberNode(NumberType.INTEGER, Integer.parseInt(stringVal));
    }

    @Override
    public <T> T accent(GuluNodeVisitor<T> visitor) {
        return visitor.visitNumberNode(this);
    }


    public enum NumberType {
        INTEGER,
        LONG,
        DOUBLE,
        FLOAT,
    }
}
