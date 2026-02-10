package ivantrendafilov_confucius;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import ivantrendafilov_confucius._98.Driver;
import java.util.List;
import java.util.Properties;

/**
 * 動的テスト: getLongList(String, String) の動作検証
 */
@RunWith(Enclosed.class)
public class Ivantrendafilov_confuciusTest_98 {

    abstract static class CommonLogic {

        abstract Driver createDriver(Properties props) throws Exception;

        @Test
        public void testGetLongListValidInput() throws Exception {
            Properties props = new Properties();
            props.setProperty("valid.list", "100,200,300");
            
            Driver driver = createDriver(props);
            
            List<Long> result = driver.getLongList("valid.list", ",");
            assertNotNull(result);
            assertEquals(3, result.size());
            assertEquals(Long.valueOf(100L), result.get(0));
            assertEquals(Long.valueOf(200L), result.get(1));
            assertEquals(Long.valueOf(300L), result.get(2));
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
