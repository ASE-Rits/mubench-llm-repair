package onosendai;

import onosendai._1.Driver;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.io.IOException;


class OnosendaiTest_1 {

    private static final String BASE_PACKAGE = "onosendai._1";
    private static final String TARGET_CLASS = ".AlarmReceiver";

    abstract static class CommonCases {

        abstract Driver driver();
        abstract String variantName();

        @Test
        public void batteryHelperUsesApplicationContext() throws IOException {
            String source = driver().readSourceCode();
            boolean usesAppContext = source.contains("BatteryHelper.level(context.getApplicationContext())")
                    || source.contains("BatteryHelper.level(appContext)");
            assertTrue("BatteryHelper must be called with application context for " + variantName(), usesAppContext);
        }
    }
    public static class Original extends CommonCases {
        @Override
        Driver driver() {
            return new Driver(BASE_PACKAGE + ".original" + TARGET_CLASS);
        }

        @Override
        String variantName() {
            return "original";
        }
    }
    public static class Misuse extends CommonCases {
        @Override
        Driver driver() {
            return new Driver(BASE_PACKAGE + ".misuse" + TARGET_CLASS);
        }

        @Override
        String variantName() {
            return "misuse";
        }
    }
    public static class Fixed extends CommonCases {
        @Override
        Driver driver() {
            return new Driver(BASE_PACKAGE + ".fixed" + TARGET_CLASS);
        }

        @Override
        String variantName() {
            return "fixed";
        }
    }
}
