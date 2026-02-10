package thomas_s_b_visualee;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import thomas_s_b_visualee._29.Driver;
import thomas_s_b_visualee._29.requirements.source.entity.JavaSource;

/**
 * 動的テスト: findAndSetPackage メソッドの動作検証
 * 
 * Case 29: Scanner.next()を呼ぶ前にhasNext()チェックが必要
 * - misuse: hasNext()チェックなしでscanner.next()を呼び出し
 * - fixed: hasNext()チェックを追加してNoSuchElementExceptionを防止
 */
@RunWith(Enclosed.class)
public class Thomas_s_b_visualeeTest_29 {

    abstract static class CommonLogic {

        abstract Driver createDriver() throws Exception;

        @Test
        public void testFindAndSetPackageIncompletePackage() throws Exception {
            Driver driver = createDriver();
            
            // "package " の後にパッケージ名がないソースコード
            // バグがある場合、hasNext()チェックなしでscanner.next()を呼び出し
            // NoSuchElementExceptionが発生する
            // 正しい実装ではIllegalArgumentExceptionを投げる（適切なエラーメッセージ付き）
            String sourceCode = "package ";
            JavaSource javaSource = new JavaSource("Test");
            javaSource.setSourceCode(sourceCode);
            
            // Original/Fixed: IllegalArgumentExceptionを投げる（正しい動作）
            // Misuse: NoSuchElementExceptionを投げる（バグ動作）
            assertThrows(IllegalArgumentException.class, () -> {
                driver.findAndSetPackage(javaSource);
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
    // testFindAndSetPackageIncompletePackageでNoSuchElementExceptionが発生し失敗する
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
