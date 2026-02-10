package lnreadera;

import lnreadera._2.Driver;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Test class for lnreadera case 2: DisplayLightNovelContentActivity
 * Tests the fix for missing super.onDestroy() call
 */
class LnreaderaTest_2 {

    private static final String BASE_PACKAGE = "lnreadera._2";

    abstract static class CommonCases {
        abstract Driver driver();

        @Test
        public void testOnDestroyCallsSuper() {
            Driver d = driver();
            boolean superCalled = d.executeOnDestroyAndCheckSuperCalled();
            assertTrue("super.onDestroy() should be called", superCalled);
        }
    }
    public static class Original extends CommonCases {
        @Override
        Driver driver() {
            return new Driver(BASE_PACKAGE + ".original.DisplayLightNovelContentActivity");
        }
    }

    // Misuseは常にコメントアウト（バグがあるため必ず失敗）
    public static class Misuse extends CommonCases {
        @Override
        Driver driver() {
            return new Driver(BASE_PACKAGE + ".misuse.DisplayLightNovelContentActivity");
        }
    }
    public static class Fixed extends CommonCases {
        @Override
        Driver driver() {
            return new Driver(BASE_PACKAGE + ".fixed.DisplayLightNovelContentActivity");
        }
    }
}
