package ivantrendafilov_confucius;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import ivantrendafilov_confucius._95.Driver;
import java.util.List;
import java.util.Properties;

/**
 * 動的テスト: getByteList(String, String) の動作検証
 */
@RunWith(Enclosed.class)
public class Ivantrendafilov_confuciusTest_95 {

    abstract static class CommonLogic {

        abstract Driver createDriver(Properties props) throws Exception;

        @Test
        public void testGetByteListValidInput() throws Exception {
            Properties props = new Properties();
            props.setProperty("valid.list", "1,2,3");
            
            Driver driver = createDriver(props);
            
            List<Byte> result = driver.getByteList("valid.list", ",");
            assertNotNull(result);
            assertEquals(3, result.size());
            assertEquals(Byte.valueOf((byte) 1), result.get(0));
            assertEquals(Byte.valueOf((byte) 2), result.get(1));
            assertEquals(Byte.valueOf((byte) 3), result.get(2));
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
