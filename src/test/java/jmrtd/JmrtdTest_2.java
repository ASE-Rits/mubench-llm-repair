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
@RunWith(Enclosed.class)
public class JmrtdTest_2 {

    private static final String BASE_PACKAGE = "jmrtd._2";

    /**
     * PassportAuthService バリアント用の共通テストケース
     */
    abstract static class PassportAuthServiceCases {

        abstract Driver driver();

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
    public static class Fixed extends PassportAuthServiceCases {

        @Override
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
