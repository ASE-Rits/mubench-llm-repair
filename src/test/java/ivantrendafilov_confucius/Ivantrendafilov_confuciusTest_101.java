package ivantrendafilov_confucius;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import ivantrendafilov_confucius._101.Driver;
import java.util.List;
import java.util.Properties;

/**
 * 動的テスト: getShortList(String, String) の動作検証
 */
@RunWith(Enclosed.class)
public class Ivantrendafilov_confuciusTest_101 {

    abstract static class CommonLogic {

        abstract Driver createDriver(Properties props) throws Exception;

        @Test
        public void testGetShortListInvalidInputContainsKey() throws Exception {
            Properties props = new Properties();
            String testKey = "invalid.list";
            props.setProperty(testKey, "10,not_a_number,30");
            
            Driver driver = createDriver(props);
            
            try {
                driver.getShortList(testKey, ",");
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
