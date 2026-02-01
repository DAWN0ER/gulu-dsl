package priv.dawn.gulu.token;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import priv.dawn.gulu.exception.IllegalGuluTokenException;
import priv.dawn.gulu.utils.TestFileLoader;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/01/23/22:30
 */
@Slf4j
public class LexerTest {

    @Test
    public void expressionLexerTest() {
        TestFileLoader.testEachExpressionExample("valid_expression.txt", (expression) -> {
            GuluLexer guluLexer = new GuluLexer(expression);
            List<GuluToken> tokenize = guluLexer.tokenize();
            Assert.assertNotSame(tokenize.get(0).getType(), GuluToken.Type.EOF);
        });
    }

    @Test
    public void wrongExpressionButValidLexerTest() {
        TestFileLoader.testEachExpressionExample("valid_lexer_invalid_expression.txt", (expression) -> {
            GuluLexer guluLexer = new GuluLexer(expression);
            List<GuluToken> tokenize = guluLexer.tokenize();
            Assert.assertNotSame(tokenize.get(0).getType(), GuluToken.Type.EOF);
        });
    }

    @Test
    public void wrongExpressionLexerTest() {
        TestFileLoader.testEachExpressionExample("invalid_lexer_expression.txt", (expression) -> {
            GuluLexer guluLexer = new GuluLexer(expression);
            Assert.assertThrows(IllegalGuluTokenException.class, guluLexer::tokenize);
        });
    }

}
