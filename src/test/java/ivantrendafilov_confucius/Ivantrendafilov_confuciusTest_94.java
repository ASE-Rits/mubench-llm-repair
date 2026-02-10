package ivantrendafilov_confucius;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import ivantrendafilov_confucius._94.Driver;
import java.util.Properties;

/**
 * 動的テスト: getByteValue(String, byte) の動作検証
 */
@RunWith(Enclosed.class)
public class Ivantrendafilov_confuciusTest_94 {

    abstract static class CommonLogic {

        abstract Driver createDriver(Properties props) throws Exception;

        @Test
        public void testGetByteValueWithDefaultInvalidInputContainsKey() throws Exception {
            Properties props = new Properties();
            String testKey = "invalid.key";
            props.setProperty(testKey, "not_a_number");
            
            Driver driver = createDriver(props);
            
            try {
                driver.getByteValue(testKey, (byte) 0);
                fail("Should throw an exception for invalid input");
            } catch (Exception ex) {
                assertTrue("Exception message should contain the key name. Got: " + ex.getMessage(),
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
