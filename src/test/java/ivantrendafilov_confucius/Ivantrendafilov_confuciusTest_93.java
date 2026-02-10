package ivantrendafilov_confucius;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import ivantrendafilov_confucius._93.Driver;
import java.util.Properties;

/**
 * 動的テスト: getByteValue(String) の動作検証
 */
@RunWith(Enclosed.class)
public class Ivantrendafilov_confuciusTest_93 {

    abstract static class CommonLogic {

        abstract Driver createDriver(Properties props) throws Exception;

        /**
         * 無効な入力に対して、例外メッセージにキー名が含まれていることを確認。
         * Original/Fixed: カスタム例外でキー名を含むメッセージ
         * Misuse: 生のNumberFormatException（キー名が含まれない）
         */
        @Test
        public void testGetByteValueInvalidInputContainsKey() throws Exception {
            Properties props = new Properties();
            String testKey = "invalid.key";
            props.setProperty(testKey, "not_a_number");
            
            Driver driver = createDriver(props);
            
            try {
                driver.getByteValue(testKey);
                fail("Should throw an exception for invalid input");
            } catch (Exception ex) {
                assertTrue("Exception message should contain the key name for debugging. " +
                    "Got: " + ex.getMessage(),
                    ex.getMessage() != null && ex.getMessage().contains(testKey));
            }
        }
    }
    public static class Original extends CommonLogic {
        @Override
        Driver createDriver(Properties props) throws Exception {
            return new Driver("original", props);
        }
    }
    public static class Misuse extends CommonLogic {
        @Override
        Driver createDriver(Properties props) throws Exception {
            return new Driver("misuse", props);
        }
    }
    public static class Fixed extends CommonLogic {
        @Override
        Driver createDriver(Properties props) throws Exception {
            return new Driver("fixed", props);
        }
    }
}
