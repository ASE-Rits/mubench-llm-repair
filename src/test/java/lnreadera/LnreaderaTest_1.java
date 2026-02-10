package lnreadera;

import lnreadera._1.Driver;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

class LnreaderaTest_1 {

    private static final String BASE_PACKAGE = "lnreadera._1";

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
            return new Driver(BASE_PACKAGE + ".original.DisplayImageActivity");
        }
    }

    // Misuseは常にコメントアウト（バグがあるため必ず失敗）
    public static class Misuse extends CommonCases {
        @Override
        Driver driver() {
            return new Driver(BASE_PACKAGE + ".misuse.DisplayImageActivity");
        }
    }
    public static class Fixed extends CommonCases {
        @Override
        Driver driver() {
            return new Driver(BASE_PACKAGE + ".fixed.DisplayImageActivity");
        }
    }
}
