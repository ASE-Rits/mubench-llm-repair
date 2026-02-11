package android_rcs_rcsjta;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import android_rcs_rcsjta._1.Driver;

/**
 * 静的テスト: ソースコードでgetBytes("UTF-8")を明示的に使用しているかを検証。
 * 
 * バグ: callId.getBytes() を引数なしで使用
 * - Original/Fixed: getBytes("UTF-8") または getBytes(StandardCharsets.UTF_8) を使用 → テストパス
 * - Misuse: getBytes() を引数なしで使用 → テストフェイル
 */
@RunWith(Enclosed.class)
public class AndroidRcsRcsjtaTest_1 {

    abstract static class CommonCases {

        abstract Driver driver();
        
        /**
         * 実装のソースファイルパスを返す。
         */
        abstract String getSourceFilePath();

        /**
         * ソースコードを検査して、getBytes("UTF-8") または getBytes(StandardCharsets.UTF_8) を
         * 使用しているかを確認する。
         * 
         * Original/Fixed: 明示的にUTF-8エンコーディングを指定 → テストパス
         * Misuse: getBytes() を引数なしで使用 → テストフェイル
         */
        @Test
        public void testSourceCodeUsesExplicitUtf8Encoding() throws Exception {
            String sourceFilePath = getSourceFilePath();
            Path path = Paths.get(sourceFilePath);
            
            assertTrue("Source file should exist: " + sourceFilePath, Files.exists(path));
            
            String sourceCode = Files.readString(path);
            
            // getContributionId メソッドを探す
            int methodStart = sourceCode.indexOf("getContributionId");
            assertTrue("getContributionId method should exist in source", methodStart >= 0);
            
            // getBytes の使用を検査
            // 正しい使い方: getBytes("UTF-8") または getBytes(StandardCharsets.UTF_8)
            // 間違った使い方: getBytes() 引数なし
            
            boolean usesExplicitUtf8 = sourceCode.contains("getBytes(\"UTF-8\")") || 
                                       sourceCode.contains("getBytes(\"UTF8\")") ||
                                       sourceCode.contains("getBytes(StandardCharsets.UTF_8)") ||
                                       sourceCode.contains("getBytes(UTF8)");
            
            assertTrue("Source code must use explicit UTF-8 encoding with getBytes(). " +
                "Using getBytes() without charset argument is platform-dependent and may cause issues. " +
                "Use getBytes(\"UTF-8\") or getBytes(StandardCharsets.UTF_8) instead.", 
                usesExplicitUtf8);
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
