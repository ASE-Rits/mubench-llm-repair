package apache_gora._56_2;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * WritableUtils#writeProperties の flush 漏れによる欠落を再現するテスト。
 * Driver 経由で Original/Fixed/Misuse を切り替えて挙動を比較する。
 */
@RunWith(Enclosed.class)
public class ApacheGoraTest_56_2 {

    private static Properties buildSampleProperties() {
        Properties props = new Properties();
        props.setProperty("keyBlah", "valueBlah");
        props.setProperty("keyBlah2", "valueBlah2");
        return props;
    }

    abstract static class CommonCase {
        abstract Driver driver();

        @Test
        public void writeThenReadKeepsAllEntries() throws Exception {
            Properties original = buildSampleProperties();
            Properties result = driver().writeThenRead(original);

            assertEquals(original, result);
            assertFalse("result should contain entries", result.isEmpty());
        }
    }
    public static class Original extends CommonCase {
        @Override
        Driver driver() {
            return new Driver("apache_gora._56_2.original.TestWritableUtils");
        }
    }
    public static class Misuse extends CommonCase {
        @Override
        Driver driver() {
            return new Driver("apache_gora._56_2.misuse.TestWritableUtils");
        }
    }
    public static class Fixed extends CommonCase {
        @Override
        Driver driver() {
            return new Driver("apache_gora._56_2.fixed.TestWritableUtils");
        }
    }
}
