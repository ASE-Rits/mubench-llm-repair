package jmrtd;

import jmrtd._2.Driver;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import javax.crypto.Cipher;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

import static org.junit.Assert.*;

/**
 * 動的テスト: PassportAuthService.doAA() の Cipher モード検証。
 * 
 * バグ: aaCipher.init() に ENCRYPT_MODE を使用（DECRYPT_MODE が正しい）
 * - Original: Cipher.DECRYPT_MODE → 署名検証が正常動作
 * - Misuse: Cipher.ENCRYPT_MODE → 署名検証が失敗
 * 
 * 注: PassportAuthService のインスタンス化には ISO9796-2 署名アルゴリズムが必要で
 * 標準 JDK では利用不可。そのため動的テストは独立した Cipher テストで実施。
 */
class JmrtdTest_2 {

    private static final String BASE_PACKAGE = "jmrtd._2";

    /**
     * PassportAuthService バリアント用の共通テストケース
     */
    abstract static class PassportAuthServiceCases {

        abstract Driver driver();

        @Test
        public void testSourceFileExists() throws IOException {
            Driver d = driver();
            String sourceCode = d.readSourceCode();
            assertNotNull(sourceCode);
            assertFalse("Source code should not be empty", sourceCode.isEmpty());
        }

        @Test
        public void testContainsPassportAuthService() throws IOException {
            Driver d = driver();
            assertTrue("Source should contain PassportAuthService class", d.containsPassportAuthService());
        }

        @Test
        public void testUsesDecryptMode() throws IOException {
            Driver d = driver();
            assertTrue("doAA() should use Cipher.DECRYPT_MODE (not ENCRYPT_MODE)", d.usesDecryptMode());
        }

        @Test
        public void testNotUsingEncryptMode() throws IOException {
            Driver d = driver();
            assertFalse("doAA() should not use Cipher.ENCRYPT_MODE", d.usesEncryptMode());
        }

        @Test
        public void testCipherModeFromSource() throws IOException {
            Driver d = driver();
            String mode = d.getCipherModeFromSource();
            assertEquals("Cipher should be initialized with DECRYPT_MODE for RSA signature verification", "DECRYPT_MODE", mode);
        }

        /**
         * 動的テスト: RSA 暗号化/復号化のラウンドトリップで DECRYPT_MODE の正しさを検証
         * 
         * doAA() のパターンを再現:
         * - カード (private key) が署名データを暗号化
         * - 検証者 (public key + DECRYPT_MODE) が復号化
         * 
         * ENCRYPT_MODE を使うと復号化が失敗する
         */
        @Test
        public void testRsaDecryptModeWorks() throws Exception {
            // Generate RSA key pair
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair kp = kpg.generateKeyPair();
            
            // Test data
            byte[] testData = new byte[100];
            for (int i = 0; i < testData.length; i++) {
                testData[i] = (byte) i;
            }
            
            // Encrypt with private key (simulating card's signature)
            Cipher encryptCipher = Cipher.getInstance("RSA");
            encryptCipher.init(Cipher.ENCRYPT_MODE, kp.getPrivate());
            byte[] encrypted = encryptCipher.doFinal(testData);
            
            // Decrypt with public key using DECRYPT_MODE (correct mode)
            Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, kp.getPublic());
            byte[] decrypted = decryptCipher.doFinal(encrypted);
            
            // Verify round-trip
            assertArrayEquals("DECRYPT_MODE should correctly decrypt data encrypted with private key", testData, decrypted);
        }

        /**
         * 動的テスト: ENCRYPT_MODE で復号化しようとすると不正な結果になることを検証
         * これが Misuse バリアントのバグパターン
         * 
         * Note: RSA で暗号化したデータをさらに ENCRYPT_MODE で処理すると
         * サイズオーバーになる場合があるため、パディングなしで検証
         */
        @Test
        public void testRsaEncryptModeProducesDifferentResult() throws Exception {
            // Generate RSA key pair
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair kp = kpg.generateKeyPair();
            
            // Small test data (to avoid RSA block size issues)
            byte[] testData = new byte[32];
            for (int i = 0; i < testData.length; i++) {
                testData[i] = (byte) i;
            }
            
            // Encrypt with private key
            Cipher encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            encryptCipher.init(Cipher.ENCRYPT_MODE, kp.getPrivate());
            byte[] encrypted = encryptCipher.doFinal(testData);
            
            // Correct way: Decrypt with DECRYPT_MODE
            Cipher correctCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            correctCipher.init(Cipher.DECRYPT_MODE, kp.getPublic());
            byte[] correctResult = correctCipher.doFinal(encrypted);
            
            // Bug way: Use ENCRYPT_MODE instead of DECRYPT_MODE
            // This simulates what happens when the misuse pattern is used
            // ENCRYPT_MODE with public key should throw or produce wrong result
            try {
                Cipher wrongCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                wrongCipher.init(Cipher.ENCRYPT_MODE, kp.getPublic());
                byte[] wrongResult = wrongCipher.doFinal(encrypted);
                
                // If we get here, the results should be different
                assertFalse("ENCRYPT_MODE should produce different result than DECRYPT_MODE", java.util.Arrays.equals(correctResult, wrongResult));
            } catch (Exception e) {
                // Exception is also acceptable - it shows the bug causes failure
                assertTrue("ENCRYPT_MODE caused exception as expected: " + e.getMessage(), true);
            }
            
            // Verify the correct result matches original
            assertArrayEquals("DECRYPT_MODE should correctly recover original data", testData, correctResult);
        }
    }
    public static class Original extends PassportAuthServiceCases {

        @Override
        Driver driver() {
            return new Driver(BASE_PACKAGE + ".original.PassportAuthService");
        }
    }

    // Misuse: ENCRYPT_MODE を使用 → ソース解析で検出
    public static class Misuse extends PassportAuthServiceCases {

        @Override
        Driver driver() {
            return new Driver(BASE_PACKAGE + ".misuse.PassportAuthService");
        }
    }

    /**
     * Fixed バリアント - LLM 失敗ケース
     */
    class Fixed {

        Driver driver() {
            return new Driver(BASE_PACKAGE + ".fixed.SecureMessagingWrapper");
        }

        @Test
        public void testSourceFileExists() throws IOException {
            Driver d = driver();
            String sourceCode = d.readSourceCode();
            assertNotNull(sourceCode);
            assertFalse("Source code should not be empty", sourceCode.isEmpty());
        }

        @Test
        public void testDoesNotContainPassportAuthService() throws IOException {
            Driver d = driver();
            assertFalse("LLM incorrectly returned SecureMessagingWrapper instead of PassportAuthService", d.containsPassportAuthService());
        }

        @Test
        public void testNoDoAAMethod() throws IOException {
            Driver d = driver();
            assertFalse("SecureMessagingWrapper doesn't have doAA method with Cipher usage", d.usesDecryptMode());
        }
    }
}
