package adempiere;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import adempiere._1.Driver;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 動的テスト: encrypt/decrypt のラウンドトリップで UTF-8 エンコーディングを検証。
 * 
 * バグ: encrypt() で getBytes() を引数なしで使用
 * - Original: getBytes("UTF8") → UTF-8 文字が正しく処理される
 * - Misuse: getBytes() → プラットフォーム依存で文字化けの可能性
 * 
 * 非ASCII文字（日本語等）を使ってラウンドトリップテストを行い、
 * 正しくエンコーディングが指定されているかを動的に検証する。
 */
@RunWith(Enclosed.class)
public class AdempiereTest_1 {

    abstract static class CommonCases {

        abstract Driver driver();
        
        /**
         * ソースコードパスを返す
         */
        abstract String getSourceFilePath();

        /**
         * 静的検査: getBytes()でUTF-8エンコーディングが明示的に指定されていることを確認
         * Misuse: getBytes() を引数なしで使用 → プラットフォーム依存
         * Original: getBytes("UTF8") または getBytes("UTF-8") を使用
         */
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
            // 次のpublicメソッドまでを抽出
            int nextMethodStart = sourceCode.indexOf("public ", encryptMethodStart + 20);
            int encryptMethodEnd = nextMethodStart > 0 ? nextMethodStart : sourceCode.length();
            String encryptMethodBody = sourceCode.substring(encryptMethodStart, encryptMethodEnd);
            
            // getBytes()呼び出しを確認
            boolean hasGetBytes = encryptMethodBody.contains(".getBytes(");
            if (hasGetBytes) {
                // getBytes("UTF8") または getBytes("UTF-8") があることを確認
                boolean hasExplicitEncoding = encryptMethodBody.contains("getBytes(\"UTF8\")") ||
                                              encryptMethodBody.contains("getBytes(\"UTF-8\")")
                                              || encryptMethodBody.contains("getBytes(StandardCharsets.UTF_8)");
                assertTrue("encrypt method must use getBytes() with explicit UTF-8 encoding. " +
                    "Using getBytes() without encoding causes platform-dependent behavior.", hasExplicitEncoding);
            }
        }
    }

    // --- 実行定義 ---
    public static class Original extends CommonCases {
        @Override
        Driver driver() {
            return new Driver(new adempiere._1.original.Secure());
        }
        @Override
        String getSourceFilePath() {
            return "src/main/java/adempiere/_1/original/Secure.java";
        }
    }

    // Misuse: getBytes() を引数なしで使用 → 非ASCII文字で失敗する可能性
    public static class Misuse extends CommonCases {
        @Override
        Driver driver() {
            return new Driver(new adempiere._1.misuse.Secure());
        }
        @Override
        String getSourceFilePath() {
            return "src/main/java/adempiere/_1/misuse/Secure.java";
        }
    }
    public static class Fixed extends CommonCases {
        @Override
        Driver driver() {
            return new Driver(new adempiere._1.fixed.Secure());
        }
        @Override
        String getSourceFilePath() {
            return "src/main/java/adempiere/_1/fixed/Secure.java";
        }
    }
}
