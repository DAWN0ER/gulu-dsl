package priv.dawn.gulu.utils;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author Dawn Yang
 * @since 2026/01/25/12:07
 */
public class TestFileLoader {

    public static void testEachExpressionExample(String exampleFilePath, Consumer<String> testFunc) {
        try {
            // 获取资源文件路径
            ClassLoader classLoader = TestFileLoader.class.getClassLoader();
            Path path = Paths.get(Objects.requireNonNull(classLoader.getResource(exampleFilePath)).toURI());
            BufferedReader br = Files.newBufferedReader(path);
            String line;
            while ((line = br.readLine()) != null) {
                testFunc.accept(line);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
