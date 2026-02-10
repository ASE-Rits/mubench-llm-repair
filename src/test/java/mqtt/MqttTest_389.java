package mqtt;

import mqtt._389.Driver;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * Test for MqttSubscribe variants (mqtt/389).
 * 
 * Bug: DataOutputStream wrapping ByteArrayOutputStream should be flushed
 * before calling toByteArray() on the underlying stream.
 * 
 * This test uses dynamic execution via Driver to verify correct behavior.
 */
@RunWith(Enclosed.class)
public class MqttTest_389 {

    private static final String[] TEST_TOPICS = {"topic/test", "another/topic"};
    private static final int[] TEST_QOS = {1, 2};

    abstract static class CommonLogic {
        abstract Driver createDriver() throws Exception;
        abstract String getSourceFilePath();

        /**
         * 静的検査: getPayload() メソッドで flush() を呼び出してから toByteArray() を呼び出していることを確認。
         * Original: dos.flush() を呼び出す → テストパス
         * Misuse: flush() を呼び出さない → テストフェイル
         */
        @Test
        public void testSourceCodeFlushesBeforeToByteArray() throws Exception {
            String sourceFilePath = getSourceFilePath();
            Path path = Paths.get(sourceFilePath);
            assertTrue("Source file should exist: " + sourceFilePath, Files.exists(path));
            
            String sourceCode = Files.readString(path);
            
            // getPayload メソッドを探す
            int methodStart = sourceCode.indexOf("public byte[] getPayload()");
            assertTrue("getPayload method should exist", methodStart >= 0);
            
            // メソッドの終わりを見つける
            int braceCount = 0;
            int methodEnd = methodStart;
            boolean foundFirstBrace = false;
            for (int i = methodStart; i < sourceCode.length(); i++) {
                char c = sourceCode.charAt(i);
                if (c == '{') {
                    braceCount++;
                    foundFirstBrace = true;
                } else if (c == '}') {
                    braceCount--;
                    if (foundFirstBrace && braceCount == 0) {
                        methodEnd = i;
                        break;
                    }
                }
            }
            
            String methodBody = sourceCode.substring(methodStart, methodEnd + 1);
            
            // toByteArray() の呼び出し位置を見つける
            int toByteArrayIndex = methodBody.indexOf("toByteArray()");
            assertTrue("toByteArray() call should exist in getPayload", toByteArrayIndex >= 0);
            
            // toByteArray() より前に flush() があることを確認
            String beforeToByteArray = methodBody.substring(0, toByteArrayIndex);
            boolean hasFlushBeforeToByteArray = beforeToByteArray.contains("dos.flush()") ||
                                                 beforeToByteArray.contains(".flush()");
            
            assertTrue("DataOutputStream must be flushed before calling toByteArray() in getPayload(). " +
                "Without flush(), buffered data may be lost.", hasFlushBeforeToByteArray);
        }
    }
    public static class Original extends CommonLogic {
        @Override
        Driver createDriver() throws Exception {
            return new Driver("original", TEST_TOPICS, TEST_QOS);
        }
        @Override
        String getSourceFilePath() {
            return "src/main/java/mqtt/_389/original/MqttSubscribe.java";
        }
    }

    public static class Misuse extends CommonLogic {
        @Override
        Driver createDriver() throws Exception {
            return new Driver("misuse", TEST_TOPICS, TEST_QOS);
        }
        @Override
        String getSourceFilePath() {
            return "src/main/java/mqtt/_389/misuse/MqttSubscribe.java";
        }
    }
    public static class Fixed extends CommonLogic {
        @Override
        Driver createDriver() throws Exception {
            return new Driver("fixed", TEST_TOPICS, TEST_QOS);
        }
        @Override
        String getSourceFilePath() {
            return "src/main/java/mqtt/_389/fixed/MqttSubscribe.java";
        }
    }
}
