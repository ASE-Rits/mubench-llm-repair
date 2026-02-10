package hoverruan_weiboclient4j;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import hoverruan_weiboclient4j._128.Driver;

/**
 * 動的テスト: cid(String) が不正な入力に対して
 * NumberFormatException をラップしてカスタムメッセージ付きの例外をスローすることを検証
 */
@RunWith(Enclosed.class)
public class HoverruanWeiboclient4jTest_128 {

    abstract static class CommonLogic {

        abstract Driver getTargetDriver();

        @Test
        public void testCidHandlesInvalidInputGracefully() {
            Driver driver = getTargetDriver();
            String invalidValue = "not_a_number";
            
            // 不正な文字列を渡した場合、エラーハンドリングされた例外がスローされるべき
            // Misuse: 生のNumberFormatException（"For input string: ..."形式）→ 失敗
            // Original/Fixed: 入力値を含む説明的なメッセージ付きの例外 → 成功
            try {
                driver.cid(invalidValue);
                fail("Should throw an exception for invalid input");
            } catch (RuntimeException e) {
                // 例外メッセージに入力値が含まれていることを確認
                // 生のNumberFormatExceptionは "For input string: X" 形式
                // ラップされた例外は入力値を含む独自メッセージ
                String message = e.getMessage();
                assertNotNull("Exception should have a message", message);
                
                // 生の NumberFormatException の形式かどうかチェック
                boolean isRawNFE = message.startsWith("For input string:");
                assertFalse("cid() should provide a helpful error message containing the invalid value, " +
                    "not the raw NumberFormatException format. Got: " + message, isRawNFE);
                
                assertTrue("Exception message should contain the invalid input value for debugging. " +
                    "Got message: " + message,
                    message.contains(invalidValue));
            }
        }
    }
    public static class Original extends CommonLogic {
        @Override
        Driver getTargetDriver() {
            return new Driver(hoverruan_weiboclient4j._128.original.CoreParameters.class);
        }
    }
    public static class Misuse extends CommonLogic {
        @Override
        Driver getTargetDriver() {
            return new Driver(hoverruan_weiboclient4j._128.misuse.CoreParameters.class);
        }
    }
    public static class Fixed extends CommonLogic {
        @Override
        Driver getTargetDriver() {
            return new Driver(hoverruan_weiboclient4j._128.fixed.CoreParameters.class);
        }
    }
}
