package android_rcs_rcsjta;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import android_rcs_rcsjta._1.Driver;

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

        /**
         * 基本的なContributionId生成テスト（ASCII callId）
         */
        @Test
        public void testConsistentResultAscii() throws Exception {
            Driver d = driver();
            String callId = "sip:12345@example.com";
            
            String result1 = d.getContributionId(callId);
            String result2 = d.getContributionId(callId);
            
            assertNotNull("ContributionId should not be null", result1);
            assertEquals("Same callId should produce same ContributionId", result1, result2);
        }

        /**
         * 日本語を含むcallIdでのテスト
         */
        @Test
        public void testConsistentResultJapanese() throws Exception {
            Driver d = driver();
            String callId = "sip:ユーザー@example.com";
            
            String result1 = d.getContributionId(callId);
            String result2 = d.getContributionId(callId);
            
            assertNotNull("ContributionId should not be null", result1);
            assertEquals("Same Japanese callId should produce same ContributionId. " +
                "Inconsistency indicates getBytes() is not using explicit UTF-8 encoding.", result1, result2);
        }

        /**
         * 中国語を含むcallIdでのテスト
         */
        @Test
        public void testConsistentResultChinese() throws Exception {
            Driver d = driver();
            String callId = "sip:用户@example.com";
            
            String result1 = d.getContributionId(callId);
            String result2 = d.getContributionId(callId);
            
            assertNotNull("ContributionId should not be null", result1);
            assertEquals("Same Chinese callId should produce same ContributionId.", result1, result2);
        }

        /**
         * ContributionIdの形式テスト（16進数32文字）
         */
        @Test
        public void testContributionIdFormat() throws Exception {
            Driver d = driver();
            String callId = "sip:test@example.com";
            
            String result = d.getContributionId(callId);
            
            assertNotNull("ContributionId should not be null", result);
            assertEquals("ContributionId should be 32 characters (128 bits)", 32, result.length());
            assertTrue("ContributionId should be lowercase hex", result.matches("[0-9a-f]+"));
        }

        /**
         * 異なるcallIdは異なるContributionIdを生成
         */
        @Test
        public void testDifferentCallIdsDifferentResults() throws Exception {
            Driver d = driver();
            
            String result1 = d.getContributionId("sip:user1@example.com");
            String result2 = d.getContributionId("sip:user2@example.com");
            
            assertNotNull(result1);
            assertNotNull(result2);
            assertNotEquals("Different callIds should produce different ContributionIds", result1, result2);
        }
    }

    // --- 実行定義 ---
    public static class Original extends CommonCases {
        @Override
        Driver driver() {
            return new Driver(android_rcs_rcsjta._1.original.ContributionIdGenerator.class);
        }
    }

    // Misuse: getBytes() を引数なしで使用
    public static class Misuse extends CommonCases {
        @Override
        Driver driver() {
            return new Driver(android_rcs_rcsjta._1.misuse.ContributionIdGenerator.class);
        }
    }
    public static class Fixed extends CommonCases {
        @Override
        Driver driver() {
            return new Driver(android_rcs_rcsjta._1.fixed.ContributionIdGenerator.class);
        }
    }
}
