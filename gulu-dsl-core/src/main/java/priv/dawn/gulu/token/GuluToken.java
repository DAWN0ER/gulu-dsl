package priv.dawn.gulu.token;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Dawn Yang
 * @since 2026/01/23/20:56
 */
@Data
@AllArgsConstructor
public class GuluToken {

    private final Type type;
    private final String value;

    public enum Type {
        // 字面量
        NUMBER,
        STRING,
        BOOLEAN,

        // 标识符
        IDENTIFIER,
        // 环境变量
        ENV_VAR,
        REFER,

        // 布尔运算符
        AND,           // AND
        OR,            // OR
        NOT,           // NOT
        EXCLAMATION,   // !

        // 比较运算符
        EQ,            // ==
        NE,            // !=
        GT,            // >
        LT,            // <
        GTE,           // >=
        LTE,           // <=

        // 关键字
        EXIST,         // EXIST 不区分大小写
        IN,            // IN 不区分大小写

        // 分隔符
        LPAREN,        // (
        RPAREN,        // )
        COMMA,         // ,
        LBRACK,        // [
        RBRACK,        // ]

        // 特殊，结束符
        EOF,
        ;
        public GuluToken of(String value){
            return new GuluToken(this,value);
        }
    }

    @Override
    public String toString() {
        return "GuluToken{" +
                "type=" + type +
                ", value='" + value + '\'' +
                '}';
    }
}
