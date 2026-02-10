package openaiab;

import openaiab._1.Driver;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(Enclosed.class)
public class OpenaiabTest_1 {

    private static final String BASE_PACKAGE = "openaiab._1";
    private static final String TARGET_CLASS = ".BillingActivity";

    abstract static class CommonCases {
        abstract Driver driver();
        abstract String variantName();

        @Test
        public void onDestroyShouldCallSuper() {
            Driver driver = driver();
            driver.resetLifecycleFlags();
            driver.onDestroy();
            assertEquals("Expected super.onDestroy() to be called for " + variantName(), true, driver.wasUnityOnDestroyCalled());
        }
    }
    public static class Original extends CommonCases {
        @Override
        Driver driver() {
            return new Driver(BASE_PACKAGE + ".original" + TARGET_CLASS);
        }

        @Override
        String variantName() { return "original"; }

    }
    public static class Misuse extends CommonCases {
        @Override
        Driver driver() {
            return new Driver(BASE_PACKAGE + ".misuse" + TARGET_CLASS);
        }

        @Override
        String variantName() { return "misuse"; }

    }
    public static class Fixed extends CommonCases {
        @Override
        Driver driver() {
            return new Driver(BASE_PACKAGE + ".fixed" + TARGET_CLASS);
        }

        @Override
        String variantName() { return "fixed"; }

    }
}
