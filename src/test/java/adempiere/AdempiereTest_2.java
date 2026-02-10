package adempiere;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import adempiere._2.Driver;

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

        @Test
        public void testRoundTripAscii() {
            Driver d = driver();
            String original = "Hello, World!";
            
            String encrypted = d.encrypt(original);
            assertNotNull("Encrypted value should not be null", encrypted);
            assertNotEquals("Encrypted should differ from original", original, encrypted);
            
            String decrypted = d.decrypt(encrypted);
            assertEquals("Decrypted should match original", original, decrypted);
        }

        @Test
        public void testRoundTripJapanese() {
            Driver d = driver();
            String original = "こんにちは世界";
            
            String encrypted = d.encrypt(original);
            assertNotNull("Encrypted value should not be null", encrypted);
            
            String decrypted = d.decrypt(encrypted);
            assertEquals("Decrypted Japanese text should match original. " +
                "Failure indicates getBytes() is not using explicit UTF-8 encoding.", original, decrypted);
        }

        @Test
        public void testRoundTripChinese() {
            Driver d = driver();
            String original = "你好世界";
            
            String encrypted = d.encrypt(original);
            assertNotNull("Encrypted value should not be null", encrypted);
            
            String decrypted = d.decrypt(encrypted);
            assertEquals("Decrypted Chinese text should match original.", original, decrypted);
        }

        @Test
        public void testRoundTripEmoji() {
            Driver d = driver();
            String original = "Hello 🌍🌎🌏";
            
            String encrypted = d.encrypt(original);
            assertNotNull("Encrypted value should not be null", encrypted);
            
            String decrypted = d.decrypt(encrypted);
            assertEquals("Decrypted emoji text should match original.", original, decrypted);
        }

        @Test
        public void testEmptyString() {
            Driver d = driver();
            String original = "";
            
            String encrypted = d.encrypt(original);
            assertNotNull("Encrypted value should not be null", encrypted);
            
            String decrypted = d.decrypt(encrypted);
            assertEquals("Empty string should round-trip correctly", original, decrypted);
        }

        @Test
        public void testRoundTripMixed() {
            Driver d = driver();
            String original = "Hello こんにちは 你好 🌍";
            
            String encrypted = d.encrypt(original);
            assertNotNull("Encrypted value should not be null", encrypted);
            
            String decrypted = d.decrypt(encrypted);
            assertEquals("Mixed text should round-trip correctly.", original, decrypted);
        }
    }

    // --- 実行定義 ---
    public static class Original extends CommonCases {
        @Override
        Driver driver() {
            return new Driver(new adempiere._2.original.Secure());
        }
    }

    // Misuse: getBytes() を引数なしで使用 → 非ASCII文字で失敗する可能性
    public static class Misuse extends CommonCases {
        @Override
        Driver driver() {
            return new Driver(new adempiere._2.misuse.Secure());
        }
    }
    public static class Fixed extends CommonCases {
        @Override
        Driver driver() {
            return new Driver(new adempiere._2.fixed.Secure());
        }
    }
}
