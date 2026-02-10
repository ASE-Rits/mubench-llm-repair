package alibaba_druid;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import alibaba_druid._1.Driver;

@RunWith(Enclosed.class)
public class AlibabaDruidTest_1 {

    /**
     * 共通のテストロジック. 
     * 外部の alibaba_druid.Driver クラスを使用します.
     * 
     * このテストは、decrypt(PublicKey, String) メソッドで Cipher インスタンスを
     * 再利用せずに新規作成しているかを検証します。
     * Original: InvalidKeyException 発生時に新しい Cipher インスタンスを作成 → テストパス
     * Misuse: Cipher インスタンスを再利用（IBM JDK で問題発生） → テストフェイル
     */
    abstract static class CommonLogic {

        abstract Driver getTargetDriver();
        
        /**
         * 実装のソースファイルパスを返す。
         */
        abstract String getSourceFilePath();

        /**
         * ソースコードを検査して、decrypt(PublicKey, String) メソッドで
         * Cipher.getInstance() を呼び出して新しいインスタンスを作成しているかを確認する。
         * 
         * Original は InvalidKeyException の catch ブロック内で Cipher.getInstance("RSA") を呼び出す → テストパス
         * Misuse は Cipher インスタンスを再利用する → テストフェイル
         */
        @Test
        public void testSourceCodeCreatesNewCipherInstance() throws Exception {
            String sourceFilePath = getSourceFilePath();
            Path path = Paths.get(sourceFilePath);
            
            assertTrue("Source file should exist: " + sourceFilePath, Files.exists(path));
            
            String sourceCode = Files.readString(path);
            
            // decrypt(PublicKey publicKey, String cipherText) メソッドを探す
            int decryptMethodStart = sourceCode.indexOf("public static String decrypt(PublicKey publicKey, String cipherText)");
            assertTrue("decrypt(PublicKey, String) method should exist in source", decryptMethodStart >= 0);
            
            // メソッドの終わりを見つける（次のpublic メソッドまで）
            int nextMethodStart = sourceCode.indexOf("public static", decryptMethodStart + 1);
            int decryptMethodEnd = nextMethodStart > 0 ? nextMethodStart : sourceCode.length();
            
            String decryptMethodBody = sourceCode.substring(decryptMethodStart, decryptMethodEnd);
            
            // catch (InvalidKeyException ブロックを見つける
            int catchBlockStart = decryptMethodBody.indexOf("catch (InvalidKeyException");
            assertTrue("InvalidKeyException catch block should exist", catchBlockStart >= 0);
            
            // catch ブロックの範囲を取得（次の catch まで、または } が閉じるまで）
            int catchBodyStart = decryptMethodBody.indexOf("{", catchBlockStart);
            int braceCount = 0;
            int catchBodyEnd = catchBodyStart;
            for (int i = catchBodyStart; i < decryptMethodBody.length(); i++) {
                char c = decryptMethodBody.charAt(i);
                if (c == '{') braceCount++;
                else if (c == '}') {
                    braceCount--;
                    if (braceCount == 0) {
                        catchBodyEnd = i;
                        break;
                    }
                }
            }
            
            String catchBlockBody = decryptMethodBody.substring(catchBodyStart, catchBodyEnd);
            
            // catch ブロック内で Cipher.getInstance を呼び出して新しいインスタンスを作成しているかチェック
            // "cipher = Cipher.getInstance" のパターンを探す（既存の変数への再代入）
            boolean createNewCipherInCatch = catchBlockBody.contains("cipher = Cipher.getInstance");
            
            assertTrue("decrypt method must create a NEW Cipher instance in the InvalidKeyException catch block. " +
                "Reusing the same Cipher instance causes issues on IBM JDK. " +
                "The catch block should contain 'cipher = Cipher.getInstance(\"RSA\")' to create a new instance.", 
                createNewCipherInCatch);
        }
    }

    // --- 以下, 実装定義 ---
    public static class Original extends CommonLogic {
        @Override
        Driver getTargetDriver() {
            return new Driver(alibaba_druid._1.original.ConfigTools.class);
        }
        
        @Override
        String getSourceFilePath() {
            return "src/main/java/alibaba_druid/_1/original/ConfigTools.java";
        }
    }

    // Misuse: テスト要件確認済み（Original はパス、Misuse はフェイル）
    // ビルドを通すためコメントアウト
    public static class Misuse extends CommonLogic {
        @Override
        Driver getTargetDriver() {
            return new Driver(alibaba_druid._1.misuse.ConfigTools.class);
        }
        
        @Override
        String getSourceFilePath() {
            return "src/main/java/alibaba_druid/_1/misuse/ConfigTools.java";
        }
    }
    public static class Fixed extends CommonLogic {
        @Override
        Driver getTargetDriver() {
            return new Driver(alibaba_druid._1.fixed.ConfigTools.class);
        }
        
        @Override
        String getSourceFilePath() {
            return "src/main/java/alibaba_druid/_1/fixed/ConfigTools.java";
        }
    }
}