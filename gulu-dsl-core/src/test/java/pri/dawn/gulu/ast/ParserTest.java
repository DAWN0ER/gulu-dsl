package pri.dawn.gulu.ast;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import pri.dawn.gulu.exception.GuluAstParserException;
import pri.dawn.gulu.token.GuluLexer;
import pri.dawn.gulu.token.GuluToken;
import pri.dawn.gulu.utils.TestFileLoader;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/01/25/12:07
 */
@Slf4j
public class ParserTest {

    @Test
    public void expressionParserTest() {
        TestFileLoader.testEachExpressionExample("valid_expression.txt", (expression) -> {
            GuluLexer guluLexer = new GuluLexer(expression);
            List<GuluToken> tokenize = guluLexer.tokenize();
            Assert.assertNotSame(tokenize.get(0).getType(), GuluToken.Type.EOF);
            Assert.assertNotNull(new GuluParser(tokenize).parser());
        });
    }

    @Test
    public void wrongExpressionParserTest() {
        TestFileLoader.testEachExpressionExample("valid_lexer_invalid_expression.txt", (expression) -> {
            GuluLexer guluLexer = new GuluLexer(expression);
            List<GuluToken> tokenize = guluLexer.tokenize();
            Assert.assertNotSame(tokenize.get(0).getType(), GuluToken.Type.EOF);
            Assert.assertThrows(GuluAstParserException.class, () -> new GuluParser(tokenize).parser());
        });
    }

}
