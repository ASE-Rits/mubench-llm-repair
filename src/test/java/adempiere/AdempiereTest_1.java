package adempiere;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import adempiere._1.Driver;

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
         * 基本的な暗号化・復号化のラウンドトリップテスト（ASCII文字）
         */
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

        /**
         * 日本語文字列でのラウンドトリップテスト
         * UTF-8 エンコーディングが正しく使用されていないと失敗する
         */
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

        /**
         * 中国語文字列でのラウンドトリップテスト
         */
        @Test
        public void testRoundTripChinese() {
            Driver d = driver();
            String original = "你好世界";
            
            String encrypted = d.encrypt(original);
            assertNotNull("Encrypted value should not be null", encrypted);
            
            String decrypted = d.decrypt(encrypted);
            assertEquals("Decrypted Chinese text should match original.", original, decrypted);
        }

        /**
         * 絵文字を含む文字列でのラウンドトリップテスト
         */
        @Test
        public void testRoundTripEmoji() {
            Driver d = driver();
            String original = "Hello 🌍🌎🌏";
            
            String encrypted = d.encrypt(original);
            assertNotNull("Encrypted value should not be null", encrypted);
            
            String decrypted = d.decrypt(encrypted);
            assertEquals("Decrypted emoji text should match original.", original, decrypted);
        }

        /**
         * 空文字列のテスト
         */
        @Test
        public void testEmptyString() {
            Driver d = driver();
            String original = "";
            
            String encrypted = d.encrypt(original);
            assertNotNull("Encrypted value should not be null", encrypted);
            
            String decrypted = d.decrypt(encrypted);
            assertEquals("Empty string should round-trip correctly", original, decrypted);
        }

        /**
         * 混合文字列（ASCII + 非ASCII）でのテスト
         */
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
            return new Driver(new adempiere._1.original.Secure());
        }
    }

    // Misuse: getBytes() を引数なしで使用 → 非ASCII文字で失敗する可能性
    // テスト確認済み: 日本語テストで失敗
    public static class Misuse extends CommonCases {
        @Override
        Driver driver() {
            return new Driver(new adempiere._1.misuse.Secure());
        }
    }
    public static class Fixed extends CommonCases {
        @Override
        Driver driver() {
            return new Driver(new adempiere._1.fixed.Secure());
        }
    }
}
