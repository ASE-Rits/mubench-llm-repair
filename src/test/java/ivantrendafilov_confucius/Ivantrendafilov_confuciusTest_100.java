package ivantrendafilov_confucius;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import ivantrendafilov_confucius._100.Driver;
import java.util.Properties;

/**
 * 動的テスト: getShortValue(String, short) の動作検証
 */
@RunWith(Enclosed.class)
public class Ivantrendafilov_confuciusTest_100 {

    abstract static class CommonLogic {

        abstract Driver createDriver(Properties props) throws Exception;

        @Test
        public void testGetShortValueWithDefaultValidInput() throws Exception {
            Properties props = new Properties();
            props.setProperty("valid.key", "1234");
            
            Driver driver = createDriver(props);
            
            short result = driver.getShortValue("valid.key", (short) 0);
            assertEquals((short) 1234, result);
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
