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
        public void testGetShortListValidInput() throws Exception {
            Properties props = new Properties();
            props.setProperty("valid.list", "10,20,30");
            
            Driver driver = createDriver(props);
            
            List<Short> result = driver.getShortList("valid.list", ",");
            assertNotNull(result);
            assertEquals(3, result.size());
            assertEquals(Short.valueOf((short) 10), result.get(0));
            assertEquals(Short.valueOf((short) 20), result.get(1));
            assertEquals(Short.valueOf((short) 30), result.get(2));
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
