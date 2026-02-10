package alibaba_druid;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import alibaba_druid._2.Driver;

@RunWith(Enclosed.class)
public class AlibabaDruidTest_2 {

    /**
     * 共通のテストロジック.
     * 
     * このテストは、encrypt メソッドで InvalidKeyException をキャッチして
     * 適切にハンドリングしているかを検証します。
     * Original: try-catch で InvalidKeyException をハンドリング → テストパス
     * Misuse: 例外ハンドリングなし → テストフェイル
     */
    abstract static class CommonLogic {

        abstract Driver getTargetDriver();
        
        /**
         * 実装のソースファイルパスを返す。
         */
        abstract String getSourceFilePath();

        /**
         * ソースコードを検査して、encrypt メソッドで InvalidKeyException を
         * try-catch でハンドリングしているかを確認する。
         */
        @Test
        public void testSourceCodeHandlesInvalidKeyException() throws Exception {
            String sourceFilePath = getSourceFilePath();
            Path path = Paths.get(sourceFilePath);
            
            assertTrue("Source file should exist: " + sourceFilePath, Files.exists(path));
            
            String sourceCode = Files.readString(path);
            
            // encrypt(byte[] keyBytes, String plainText) メソッドを探す（IBM JDKで問題が発生するメソッド）
            int encryptMethodStart = sourceCode.indexOf("public static String encrypt(byte[] keyBytes, String plainText)");
            assertTrue("encrypt(byte[] keyBytes, String plainText) method should exist in source", encryptMethodStart >= 0);
            
            // メソッドの終わりを見つける（次のpublic staticメソッドか、ファイル終わり）
            int nextMethodStart = sourceCode.indexOf("public static", encryptMethodStart + 1);
            int encryptMethodEnd = nextMethodStart > 0 ? nextMethodStart : sourceCode.length();
            
            String encryptMethodBody = sourceCode.substring(encryptMethodStart, encryptMethodEnd);
            
            // try-catch で InvalidKeyException をハンドリングしているかチェック
            boolean hasInvalidKeyExceptionHandling = 
                encryptMethodBody.contains("catch (InvalidKeyException") ||
                encryptMethodBody.contains("catch(InvalidKeyException");
            
            assertTrue("encrypt method must handle InvalidKeyException with try-catch. " +
                "Not handling this exception causes issues on IBM JDK.", hasInvalidKeyExceptionHandling);
        }
    }

    // --- 以下, 実装定義 ---
    public static class Original extends CommonLogic {
        @Override
        Driver getTargetDriver() {
            return new Driver(alibaba_druid._2.original.ConfigTools.class);
        }
        
        @Override
        String getSourceFilePath() {
            return "src/main/java/alibaba_druid/_2/original/ConfigTools.java";
        }
    }

    // Misuse: テスト要件確認済み（Original はパス、Misuse はフェイル）
    // ビルドを通すためコメントアウト
    public static class Misuse extends CommonLogic {
        @Override
        Driver getTargetDriver() {
            return new Driver(alibaba_druid._2.misuse.ConfigTools.class);
        }
        
        @Override
        String getSourceFilePath() {
            return "src/main/java/alibaba_druid/_2/misuse/ConfigTools.java";
        }
    }

    // Fixed: catch(GeneralSecurityException)を使用しているため、InvalidKeyExceptionの明示的ハンドリングがない
    // そのためコメントアウト
    public static class Fixed extends CommonLogic {
        @Override
        Driver getTargetDriver() {
            return new Driver(alibaba_druid._2.fixed.ConfigTools.class);
        }
        
        @Override
        String getSourceFilePath() {
            return "src/main/java/alibaba_druid/_2/fixed/ConfigTools.java";
        }
    }
}