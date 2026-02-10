package thomas_s_b_visualee;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import thomas_s_b_visualee._30.Driver;
import java.util.Scanner;

/**
 * 動的テスト: jumpOverJavaToken メソッドの動作検証
 * 
 * Case 30: Scanner.next()を呼ぶ前にhasNext()チェックが必要
 * - misuse: hasNext()チェックなしでscanner.next()を呼び出し
 * - fixed: hasNext()チェックを追加してNoSuchElementExceptionを防止
 */
@RunWith(Enclosed.class)
public class Thomas_s_b_visualeeTest_30 {

    abstract static class CommonLogic {

        abstract Driver createDriver() throws Exception;

        @Test
        public void testJumpOverJavaTokenLoneTokenAtEOF() throws Exception {
            Driver driver = createDriver();
            
            // Javaトークンのみで次のトークンがないソースコード
            // バグがある場合、hasNext()チェックなしでscanner.next()を呼び出し
            // NoSuchElementExceptionが発生する
            // 正しい実装ではIllegalArgumentExceptionを投げる（適切なエラーメッセージ付き）
            String sourceCode = "void";
            Scanner scanner = driver.getSourceCodeScanner(sourceCode);
            String token = scanner.next();  // "void"
            
            // Original/Fixed: IllegalArgumentExceptionを投げる（正しい動作）
            // Misuse: NoSuchElementExceptionを投げる（バグ動作）
            assertThrows(IllegalArgumentException.class, () -> {
                driver.jumpOverJavaToken(token, scanner);
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
    // testJumpOverJavaTokenLoneTokenAtEOFでNoSuchElementExceptionが発生し失敗する
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
