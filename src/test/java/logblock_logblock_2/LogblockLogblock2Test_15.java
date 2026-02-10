package logblock_logblock_2;

import logblock_logblock_2._15.Driver;
import logblock_logblock_2._15.requirements.entry.blob.PaintingBlob;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * Test for logblock-logblock-2 case 15.
 * 
 * Bug: DataOutputStream wrapping ByteArrayOutputStream should be flushed/closed
 * before calling toByteArray() on the underlying ByteArrayOutputStream.
 * 
 * This test uses dynamic execution via Driver to verify correct behavior.
 */
@RunWith(Enclosed.class)
public class LogblockLogblock2Test_15 {

    abstract static class CommonLogic {
        abstract Driver createDriver() throws Exception;
        abstract String getSourceFilePath();

        /**
         * 静的検査: DataOutputStream を close() してから toByteArray() を呼び出していることを確認。
         * Original: outStream.close() を呼び出す → テストパス
         * Misuse: close() を呼び出さない → テストフェイル
         */
        @Test
        public void testSourceCodeClosesStreamBeforeToByteArray() throws Exception {
            String sourceFilePath = getSourceFilePath();
            Path path = Paths.get(sourceFilePath);
            assertTrue("Source file should exist: " + sourceFilePath, Files.exists(path));
            
            String sourceCode = Files.readString(path);
            
            // paintingTest メソッドを探す
            int methodStart = sourceCode.indexOf("public void paintingTest()");
            assertTrue("paintingTest method should exist", methodStart >= 0);
            
            int methodEnd = sourceCode.indexOf("}", methodStart + 1);
            while (methodEnd > 0) {
                // Check if this is the end of the method by counting braces
                String methodBody = sourceCode.substring(methodStart, methodEnd + 1);
                int openBraces = 0;
                for (char c : methodBody.toCharArray()) {
                    if (c == '{') openBraces++;
                    else if (c == '}') openBraces--;
                }
                if (openBraces <= 0) break;
                methodEnd = sourceCode.indexOf("}", methodEnd + 1);
            }
            
            String methodBody = sourceCode.substring(methodStart, methodEnd + 1);
            
            // toByteArray() の呼び出し位置を見つける
            int toByteArrayIndex = methodBody.indexOf("toByteArray()");
            assertTrue("toByteArray() call should exist", toByteArrayIndex >= 0);
            
            // toByteArray() より前に close() または flush() があることを確認
            String beforeToByteArray = methodBody.substring(0, toByteArrayIndex);
            boolean hasCloseBeforeToByteArray = beforeToByteArray.contains("outStream.close()") ||
                                                 beforeToByteArray.contains(".close()");
            
            assertTrue("DataOutputStream must be closed before calling toByteArray(). " +
                "Without close(), buffered data may be lost.", hasCloseBeforeToByteArray);
        }
    }
    public static class Original extends CommonLogic {
        @Override
        Driver createDriver() throws Exception {
            return new Driver("original");
        }
        @Override
        String getSourceFilePath() {
            return "src/main/java/logblock_logblock_2/_15/original/BlobTest.java";
        }
    }

    public static class Misuse extends CommonLogic {
        @Override
        Driver createDriver() throws Exception {
            return new Driver("misuse");
        }
        @Override
        String getSourceFilePath() {
            return "src/main/java/logblock_logblock_2/_15/misuse/BlobTest.java";
        }
    }
    public static class Fixed extends CommonLogic {
        @Override
        Driver createDriver() throws Exception {
            return new Driver("fixed");
        }
        @Override
        String getSourceFilePath() {
            return "src/main/java/logblock_logblock_2/_15/fixed/BlobTest.java";
        }
    }
}
