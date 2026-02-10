package asterisk_java._81;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Exercises AsyncAgiEvent decoding behaviour via the Driver helper.
 */
@RunWith(Enclosed.class)
public class AsteriskJavaTest_81 {

    private static final String ENCODED_ENV =
        "agi_channel%3A%20%E2%9C%93\n" +
        "agi_language%3A%20ja";

    private static final String ENCODED_RESULT = "200%20result%3d0";

    abstract static class CommonCase {
        abstract Driver driver();

        @Test
        public void decodeEnv() throws Exception {
            List<String> lines = driver().decodeEnv(ENCODED_ENV);
            assertFalse("decoded env should not be empty", lines.isEmpty());
            assertEquals("agi_channel: ✓", lines.get(0));
        }
    }
    public static class Original extends CommonCase {
        @Override
        Driver driver() {
            return new Driver("asterisk_java._81.original.AsyncAgiEvent");
        }
    }
    public static class Misuse extends CommonCase {
        @Override
        Driver driver() {
            return new Driver("asterisk_java._81.misuse.AsyncAgiEvent");
        }
    }
    public static class Fixed extends CommonCase {
        @Override
        Driver driver() {
            return new Driver("asterisk_java._81.fixed.AsyncAgiEvent");
        }
    }
}
