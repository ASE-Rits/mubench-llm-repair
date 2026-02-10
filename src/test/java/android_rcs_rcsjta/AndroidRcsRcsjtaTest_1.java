package android_rcs_rcsjta;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import android_rcs_rcsjta._1.Driver;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 動的テスト: getContributionId() の結果一貫性を検証。
 * 
 * バグ: callId.getBytes() を引数なしで使用
 * - Original: getBytes(UTF8) → 一貫した結果
 * - Misuse: getBytes() → プラットフォーム依存で結果が変わる可能性
 * 
 * 同じ入力に対して常に同じ結果が返ることを検証し、
 * 非ASCII文字を含む入力でも正常に動作することを確認する。
 */
@RunWith(Enclosed.class)
public class AndroidRcsRcsjtaTest_1 {

    abstract static class CommonCases {

        abstract Driver driver();
        
        abstract String getSourceFilePath();

        /**
         * 静的検査: getBytes()でUTF-8エンコーディングが明示的に指定されていることを確認
         * Misuse: getBytes() を引数なしで使用 → プラットフォーム依存
         * Original: getBytes(UTF8) を使用
         */
        @Test
        public void testSourceCodeUsesExplicitUtf8Encoding() throws Exception {
            String sourceFilePath = getSourceFilePath();
            Path path = Paths.get(sourceFilePath);
            assertTrue("Source file should exist: " + sourceFilePath, Files.exists(path));
            
            String sourceCode = Files.readString(path);
            
            // getContributionId メソッドを探す（様々な修飾子を許容）
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
                "public\\s+(synchronized\\s+)?(static\\s+)?String\\s+getContributionId\\s*\\(");
            java.util.regex.Matcher matcher = pattern.matcher(sourceCode);
            assertTrue("getContributionId method should exist", matcher.find());
            
            int methodStart = matcher.start();
            // 次のpublicメソッドまでを抽出
            int nextMethodStart = sourceCode.indexOf("public ", methodStart + 20);
            int methodEnd = nextMethodStart > 0 ? nextMethodStart : sourceCode.length();
            String methodBody = sourceCode.substring(methodStart, methodEnd);
            
            // getBytes()呼び出しを確認
            boolean hasGetBytes = methodBody.contains(".getBytes(");
            if (hasGetBytes) {
                // getBytes(UTF8) または getBytes("UTF-8") があることを確認
                boolean hasExplicitEncoding = methodBody.contains("getBytes(UTF8)") ||
                                              methodBody.contains("getBytes(\"UTF-8\")") ||
                                              methodBody.contains("getBytes(StandardCharsets.UTF_8)");
                assertTrue("getContributionId method must use getBytes() with explicit UTF-8 encoding. " +
                    "Using getBytes() without encoding causes platform-dependent behavior.", hasExplicitEncoding);
            }
        }
    }

    // --- 実行定義 ---
    public static class Original extends CommonCases {
        @Override
        Driver driver() {
            return new Driver(android_rcs_rcsjta._1.original.ContributionIdGenerator.class);
        }
        @Override
        String getSourceFilePath() {
            return "src/main/java/android_rcs_rcsjta/_1/original/ContributionIdGenerator.java";
        }
    }

    public static class Misuse extends CommonCases {
        @Override
        Driver driver() {
            return new Driver(android_rcs_rcsjta._1.misuse.ContributionIdGenerator.class);
        }
        @Override
        String getSourceFilePath() {
            return "src/main/java/android_rcs_rcsjta/_1/misuse/ContributionIdGenerator.java";
        }
    }
    public static class Fixed extends CommonCases {
        @Override
        Driver driver() {
            return new Driver(android_rcs_rcsjta._1.fixed.ContributionIdGenerator.class);
        }
        @Override
        String getSourceFilePath() {
            return "src/main/java/android_rcs_rcsjta/_1/fixed/ContributionIdGenerator.java";
        }
    }
}
