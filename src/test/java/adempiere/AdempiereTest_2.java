package adempiere;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import adempiere._2.Driver;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 動的テスト: encrypt/decrypt のラウンドトリップで UTF-8 エンコーディングを検証。
 * 
 * バグ: encrypt() で getBytes() を引数なしで使用
 * - Original: getBytes("UTF8") → UTF-8 文字が正しく処理される
 * - Misuse: getBytes() → プラットフォーム依存で文字化けの可能性
 */
@RunWith(Enclosed.class)
public class AdempiereTest_2 {

    abstract static class CommonCases {

        abstract Driver driver();
        
        abstract String getSourceFilePath();

        @Test
        public void testSourceCodeUsesExplicitUtf8Encoding() throws Exception {
            String sourceFilePath = getSourceFilePath();
            Path path = Paths.get(sourceFilePath);
            assertTrue("Source file should exist: " + sourceFilePath, Files.exists(path));
            
            String sourceCode = Files.readString(path);
            
            // encrypt(String) メソッドを探す（空白を許容）
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
                "public\\s+String\\s+encrypt\\s*\\(\\s*String");
            java.util.regex.Matcher matcher = pattern.matcher(sourceCode);
            assertTrue("encrypt(String) method should exist", matcher.find());
            
            int encryptMethodStart = matcher.start();
            int nextMethodStart = sourceCode.indexOf("public ", encryptMethodStart + 20);
            int encryptMethodEnd = nextMethodStart > 0 ? nextMethodStart : sourceCode.length();
            String encryptMethodBody = sourceCode.substring(encryptMethodStart, encryptMethodEnd);
            
            boolean hasGetBytes = encryptMethodBody.contains(".getBytes(");
            if (hasGetBytes) {
                boolean hasExplicitEncoding = encryptMethodBody.contains("getBytes(\"UTF8\")") ||
                                              encryptMethodBody.contains("getBytes(\"UTF-8\")") ||
                                              encryptMethodBody.contains("getBytes(StandardCharsets.UTF_8)");
                assertTrue("encrypt method must use getBytes() with explicit UTF-8 encoding.", hasExplicitEncoding);
            }
        }
    }

    // --- 実行定義 ---
    public static class Original extends CommonCases {
        @Override
        Driver driver() {
            return new Driver(new adempiere._2.original.Secure());
        }
        @Override
        String getSourceFilePath() {
            return "src/main/java/adempiere/_2/original/Secure.java";
        }
    }

    public static class Misuse extends CommonCases {
        @Override
        Driver driver() {
            return new Driver(new adempiere._2.misuse.Secure());
        }
        @Override
        String getSourceFilePath() {
            return "src/main/java/adempiere/_2/misuse/Secure.java";
        }
    }
    public static class Fixed extends CommonCases {
        @Override
        Driver driver() {
            return new Driver(new adempiere._2.fixed.Secure());
        }
        @Override
        String getSourceFilePath() {
            return "src/main/java/adempiere/_2/fixed/Secure.java";
        }
    }
}
