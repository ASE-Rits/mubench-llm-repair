package thomas_s_b_visualee;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import thomas_s_b_visualee._32.Driver;
import java.util.Scanner;

/**
 * 動的テスト: scanAfterClosedParenthesis メソッドの動作検証
 * 
 * Case 32: Scanner.next()を呼ぶ前にhasNext()チェックが必要
 * - misuse: hasNext()チェックなしでscanner.next()を呼び出し
 * - fixed: hasNext()チェックを追加してNoSuchElementExceptionを防止
 */
@RunWith(Enclosed.class)
public class Thomas_s_b_visualeeTest_32 {

    abstract static class CommonLogic {

        abstract Driver createDriver() throws Exception;

        @Test
        public void testScanAfterClosedParenthesisBalanced() throws Exception {
            Driver driver = createDriver();
            
            // Token with balanced parentheses should just return next token
            String sourceCode = "(test) nextToken";
            Scanner scanner = driver.getSourceCodeScanner(sourceCode);
            String token = scanner.next();  // "(test)"
            
            String result = driver.scanAfterClosedParenthesis(token, scanner);
            
            assertEquals("nextToken", result);
        }

        @Test
        public void testScanAfterClosedParenthesisNested() throws Exception {
            Driver driver = createDriver();
            
            // Unbalanced open paren, need to scan until closed
            String sourceCode = "(arg1, inner ) result";
            Scanner scanner = driver.getSourceCodeScanner(sourceCode);
            String token = scanner.next();  // "(arg1,"
            
            String result = driver.scanAfterClosedParenthesis(token, scanner);
            
            // Should scan past the closing parenthesis
            assertNotNull(result);
        }

        @Test
        public void testScanAfterClosedParenthesisUnclosedAtEOF() throws Exception {
            Driver driver = createDriver();
            
            // 開き括弧の後にトークンがないソースコード
            // バグがある場合、hasNext()チェックなしでscanner.next()を呼び出し
            // NoSuchElementExceptionが発生する
            // 正しい実装ではIllegalArgumentExceptionを投げる（適切なエラーメッセージ付き）
            String sourceCode = "(";
            Scanner scanner = driver.getSourceCodeScanner(sourceCode);
            String token = scanner.next();  // "("
            
            // Original/Fixed: IllegalArgumentExceptionを投げる（正しい動作）
            // Misuse: NoSuchElementExceptionを投げる（バグ動作）
            assertThrows(IllegalArgumentException.class, () -> {
                driver.scanAfterClosedParenthesis(token, scanner);
            });
        }
    }
    public static class Original extends CommonLogic {
        @Override
        Driver createDriver() throws Exception {
            return Driver.createOriginal();
        }
    }

    // Misuseはバグ（hasNext()チェックなしでscanner.next()を呼び出し）があるため
    // testScanAfterClosedParenthesisUnclosedAtEOFでNoSuchElementExceptionが発生し失敗する
    public static class Misuse extends CommonLogic {
        @Override
        Driver createDriver() throws Exception {
            return Driver.createMisuse();
        }
    }

    // Fixedはバグ修正に失敗したためコメントアウト
    public static class Fixed extends CommonLogic {
        @Override
        Driver createDriver() throws Exception {
            return Driver.createFixed();
        }
    }
}
