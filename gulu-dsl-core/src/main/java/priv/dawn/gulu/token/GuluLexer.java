package priv.dawn.gulu.token;

import lombok.extern.slf4j.Slf4j;
import priv.dawn.gulu.exception.IllegalGuluTokenException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dawn Yang
 * @since 2026/01/23/20:58
 */
@Slf4j
public class GuluLexer {

    private static final char EOF = '\0';

    private final String expression;
    private int position;
    private char currentChar;

    public GuluLexer(String expression) {
        this.expression = expression;
        this.position = 0;
        this.currentChar = expression.isEmpty() ? EOF : expression.charAt(0);
    }

    private void advance() {
        position++;
        this.currentChar = expression.length() > position ? expression.charAt(position) : EOF;
    }

    private char peekNext() {
        return expression.length() > position + 1 ? expression.charAt(position + 1) : EOF;
    }

    private void skipWhiteSpace() {
        while (Character.isWhitespace(currentChar)) {
            advance();
        }
    }

    private boolean anyMatch(char... chars) {
        for (char aChar : chars) {
            if (aChar == currentChar) {
                return true;
            }
        }
        return false;
    }

    private boolean isPathOrKeyWordBeginChar(char aChar) {
        return Character.isLetter(aChar) || aChar == '_';
    }

    private String matchPath() {
        if (isPathOrKeyWordBeginChar(currentChar)) {
            StringBuilder sb = new StringBuilder();
            do {
                do {
                    sb.append(currentChar);
                    advance();
                } while (Character.isLetterOrDigit(currentChar) || anyMatch('_'));
            } while (currentChar == '.' && isPathOrKeyWordBeginChar(peekNext()));
            return sb.toString();
        }
        throw new IllegalGuluTokenException(String.format("chars begin from %d can't be matched as PATH", position));
    }

    public List<GuluToken> tokenize() {
        List<GuluToken> tokenList = new ArrayList<>();
        GuluToken next;
        do {
            next = nextToken();
            tokenList.add(next);
        } while (next.getType() != GuluToken.Type.EOF);
        skipWhiteSpace();
        if (position != expression.length()) {
            throw new IllegalGuluTokenException(String.format("Unexpected char:%s at position:%d", currentChar, position));
        }
        return tokenList;
    }

    private GuluToken nextToken() {
        skipWhiteSpace();
        // 最长匹配，优先匹配，先匹配标识符和关键字
        if (isPathOrKeyWordBeginChar(currentChar)) {
            String matchStr = matchPath();
            if (matchStr.equalsIgnoreCase("and")) {
                return GuluToken.Type.AND.of(matchStr);
            }
            if (matchStr.equalsIgnoreCase("or")) {
                return GuluToken.Type.OR.of(matchStr);
            }
            if (matchStr.equalsIgnoreCase("not")) {
                return GuluToken.Type.NOT.of(matchStr);
            }
            if (matchStr.equalsIgnoreCase("in")) {
                return GuluToken.Type.IN.of(matchStr);
            }
            if (matchStr.equalsIgnoreCase("true")) {
                return GuluToken.Type.BOOLEAN.of(matchStr);
            }
            if (matchStr.equalsIgnoreCase("false")) {
                return GuluToken.Type.BOOLEAN.of(matchStr);
            }
            if (matchStr.equalsIgnoreCase("exist")) {
                return GuluToken.Type.EXIST.of(matchStr);
            }
            return GuluToken.Type.IDENTIFIER.of(matchStr);
        }
        // 匹配环境变量/引用
        if (currentChar == '#') {
            advance();
            StringBuilder sb = new StringBuilder();
            sb.append('#');
            // 环境变量
            if (isPathOrKeyWordBeginChar(currentChar)) {
                return GuluToken.Type.ENV_VAR.of(sb.append(matchPath()).toString());
            }
            // 引用
            if (currentChar == '{' && isPathOrKeyWordBeginChar(peekNext())) {
                advance();
                sb.append('{');
                sb.append(matchPath());
                if (currentChar == '}') {
                    advance();
                    return GuluToken.Type.REFER.of(sb.append('}').toString());
                }
                throw new IllegalGuluTokenException("Reference must end with '}', position:" + position);
            }
            throw new IllegalGuluTokenException("Unexpected char:" + currentChar);
        }

        // 匹配字符串
        if (anyMatch('\'', '"')) {
            char quota = currentChar;
            advance();
            StringBuilder sb = new StringBuilder();
            while (currentChar != quota && currentChar != EOF) {
                if (currentChar == '\\' && peekNext() == quota) {
                    advance();
                }
                sb.append(currentChar);
                advance();
            }
            advance();
            return GuluToken.Type.STRING.of(sb.toString());
        }
        // 匹配数字
        if (Character.isDigit(currentChar) || currentChar == '-') {
            StringBuilder sb = new StringBuilder();
            if (currentChar == '-') {
                sb.append(currentChar);
                advance();
            }
            int digitCount = 0;
            while (Character.isDigit(currentChar)) {
                sb.append(currentChar);
                advance();
                digitCount++;
            }
            if (digitCount == 0) {
                throw new IllegalGuluTokenException("Number must have more then one digit, position:" + position);
            }
            if (currentChar == '.') {
                sb.append(currentChar);
                digitCount = 0;
                advance();
            }
            while (Character.isDigit(currentChar)) {
                sb.append(currentChar);
                advance();
                digitCount++;
            }
            if (digitCount == 0) {
                throw new IllegalGuluTokenException("Number must have more then one digit, position:" + position);
            }
            if (anyMatch('d', 'D', 'f', 'F', 'l', 'L')) {
                sb.append(currentChar);
                advance();
            }
            return GuluToken.Type.NUMBER.of(sb.toString());
        }
        // 匹配双字符操作符
        if (currentChar == '=' && peekNext() == '=') {
            advance();
            advance();
            return GuluToken.Type.EQ.of("==");
        }
        if (currentChar == '!' && peekNext() == '=') {
            advance();
            advance();
            return GuluToken.Type.NE.of("!=");
        }
        if (currentChar == '<' && peekNext() == '=') {
            advance();
            advance();
            return GuluToken.Type.LTE.of("<=");
        }
        if (currentChar == '>' && peekNext() == '=') {
            advance();
            advance();
            return GuluToken.Type.GTE.of(">=");
        }
        // 匹配单字符操作符
        if (currentChar == '!') {
            advance();
            return GuluToken.Type.EXCLAMATION.of("!");
        }
        if (currentChar == '>') {
            advance();
            return GuluToken.Type.GT.of(">");
        }
        if (currentChar == '<') {
            advance();
            return GuluToken.Type.LT.of("<");
        }
        if (currentChar == '(') {
            advance();
            return GuluToken.Type.LPAREN.of("(");
        }
        if (currentChar == ')') {
            advance();
            return GuluToken.Type.RPAREN.of(")");
        }
        if (currentChar == ',') {
            advance();
            return GuluToken.Type.COMMA.of(",");
        }
        if (currentChar == '[') {
            advance();
            return GuluToken.Type.LBRACK.of("[");
        }
        if (currentChar == ']') {
            advance();
            return GuluToken.Type.RBRACK.of("]");
        }
        return GuluToken.Type.EOF.of(String.valueOf(EOF));
    }


}
