package priv.dawn.gulu.ast;

import priv.dawn.gulu.ast.node.*;
import priv.dawn.gulu.exception.GuluAstParserException;
import priv.dawn.gulu.token.GuluToken;

import java.util.ArrayList;
import java.util.List;

import static priv.dawn.gulu.token.GuluToken.Type.*;

/**
 * @author Dawn Yang
 * @since 2026/01/23/22:59
 */
public class GuluParser {

    private final GuluToken EOF_TOKEN = GuluToken.Type.EOF.of("\0");

    private final List<GuluToken> tokens;
    private int index;
    private GuluToken currentToken;

    public GuluParser(List<GuluToken> tokens) {
        this.tokens = new ArrayList<>(tokens);
        this.index = 0;
        this.currentToken = tokens.isEmpty() ? EOF_TOKEN : tokens.get(0);
    }

    private void advance() {
        index++;
        currentToken = index > tokens.size() ? EOF_TOKEN : tokens.get(index);
    }

    private GuluToken consume(GuluToken.Type tokenType) {
        if (currentToken.getType() == tokenType) {
            GuluToken token = currentToken;
            advance();
            return token;
        }
        throw new GuluAstParserException(String.format("Unexpected token at %d, excepted token type:%s", index, tokenType));
    }

    private boolean check(GuluToken.Type... types) {
        for (GuluToken.Type type : types) {
            if (type == currentToken.getType()) {
                return true;
            }
        }
        return false;
    }

    private GuluToken.Type peekNext() {
        int temp = index + 1;
        GuluToken token = temp > tokens.size() ? EOF_TOKEN : tokens.get(temp);
        return token.getType();
    }

    public GuluAstNode parser() {
        GuluEvalBoolNode expression = parserOrExpression();
        if (currentToken.getType() != EOF) {
            throw new GuluAstParserException(String.format("Token parser end at %d, but the last token's type is %s rather than EOF", index, currentToken.getType()));
        }
        return expression;
    }

    private GuluEvalBoolNode parserOrExpression() {
        List<GuluEvalBoolNode> opends = new ArrayList<>();
        opends.add(parserAndExpression());
        while (check(OR)) {
            advance();
            opends.add(parserAndExpression());
        }
        return opends.size() > 1 ? GuluBoolLogicNode.or(opends) : opends.get(0);
    }

    private GuluEvalBoolNode parserAndExpression() {
        List<GuluEvalBoolNode> opends = new ArrayList<>();
        opends.add(parserNotExpression());
        while (check(AND)) {
            advance();
            opends.add(parserNotExpression());
        }
        return opends.size() > 1 ? GuluBoolLogicNode.and(opends) : opends.get(0);
    }

    private GuluEvalBoolNode parserNotExpression() {
        if (check(NOT, EXCLAMATION)) {
            advance();
            GuluEvalBoolNode notExpression = parserNotExpression();
            if (notExpression instanceof GuluBoolNotNode) {
                return ((GuluBoolNotNode) notExpression).getWrappedNode();
            } else {
                return new GuluBoolNotNode(notExpression);
            }
        }
        return parserBaseBoolExpression();
    }

    private GuluEvalBoolNode parserBaseBoolExpression() {
        // exist 函数
        if (check(EXIST)) {
            advance();
            consume(LPAREN);
            GuluToken token = consume(IDENTIFIER);
            consume(RPAREN);
            return new GuluExistedNode(new GuluIdentifierNode(token.getValue()));
        }
        // 标识符开头
        if (check(IDENTIFIER)) {
            GuluIdentifierNode identifier = new GuluIdentifierNode(currentToken.getValue());
            advance();
            // 比较表达式
            if (check(EQ, NE, GT, LT, GTE, LTE)) {
                GuluToken.Type type = currentToken.getType();
                advance();
                GuluAstNode literal = parserLiteral();
                return new GuluBinaryCompareNode(identifier, type, literal);
            }
            // IN
            if (check(IN)) {
                advance();
                consume(LPAREN);
                List<GuluAstNode> literalList = parserLiteralList();
                consume(RPAREN);
                return new GuluInFuncNode(identifier, literalList);
            }
            // a[1,2] contains | nested.object[a1=1 and a2 = 2] expression
            if (check(LBRACK)) {
                advance();
                if (check(BOOLEAN, STRING, NUMBER, ENV_VAR) && (peekNext() == COMMA || peekNext() == RBRACK)) {
                    List<GuluAstNode> literalList = parserLiteralList();
                    consume(RBRACK);
                    return new GuluContainsNode(identifier, literalList);
                }
                GuluEvalBoolNode expression = parserOrExpression();
                consume(RBRACK);
                return new GuluNestedNode(identifier, expression);
            }
        }
        // literal compare identifier
        if (check(NUMBER, STRING, ENV_VAR, BOOLEAN)) {
            GuluAstNode literal = parserLiteral();
            if (check(EQ, NE, GT, LT, GTE, LTE)) {
                GuluToken.Type type = currentToken.getType();
                advance();
                GuluToken token = consume(IDENTIFIER);
                return new GuluBinaryCompareNode(literal, type, new GuluIdentifierNode(token.getValue()));
            }
            throw new GuluAstParserException(String.format("Unexpected token at %d, excepted token type:IDENTIFIER", index));
        }
        // refer
        if (check(REFER)) {
            GuluReferNode referNode = new GuluReferNode(currentToken.getValue());
            advance();
            return referNode;
        }
        // '(' expression ')'
        if (check(LPAREN)) {
            advance();
            GuluEvalBoolNode expression = parserOrExpression();
            consume(RPAREN);
            return expression;
        }
        throw new GuluAstParserException(String.format("Unexpected token at %d", index));
    }

    private List<GuluAstNode> parserLiteralList() {
        List<GuluAstNode> literalList = new ArrayList<>();
        literalList.add(parserLiteral());
        while (check(COMMA)) {
            advance();
            literalList.add(parserLiteral());
        }
        return literalList;
    }

    private GuluAstNode parserLiteral() {
        if (check(NUMBER)) {
            GuluNumberNode numberNode = GuluNumberNode.parerValue(currentToken.getValue());
            advance();
            return numberNode;
        }
        if (check(STRING)) {
            GuluStringNode stringNode = new GuluStringNode(currentToken.getValue());
            advance();
            return stringNode;
        }
        if (check(BOOLEAN)) {
            GuluBooleanNode booleanNode = new GuluBooleanNode(currentToken.getValue());
            advance();
            return booleanNode;
        }
        if (check(ENV_VAR)) {
            GuluEnvVarNode envVarNode = new GuluEnvVarNode(currentToken.getValue());
            advance();
            return envVarNode;
        }
        throw new GuluAstParserException(String.format("Unexpected token at %d, excepted token type:NUMBER/STRING/BOOLEAN/ENV_VAR", index));
    }

}
