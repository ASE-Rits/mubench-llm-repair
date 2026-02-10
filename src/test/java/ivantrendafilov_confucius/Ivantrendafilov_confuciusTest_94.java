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
        public void testGetByteValueWithDefaultValidInput() throws Exception {
            Properties props = new Properties();
            props.setProperty("valid.key", "42");
            
            Driver driver = createDriver(props);
            
            byte result = driver.getByteValue("valid.key", (byte) 0);
            assertEquals((byte) 42, result);
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
