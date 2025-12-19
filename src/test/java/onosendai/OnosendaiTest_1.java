package onosendai;

import onosendai._1.Driver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class OnosendaiTest_1 {

    private static final String BASE_PACKAGE = "onosendai._1";
    private static final String TARGET_CLASS = ".AlarmReceiver";

    abstract static class CommonCases {

        abstract Driver driver();
        abstract String variantName();

        @Test
        @DisplayName("BatteryHelper must use application context")
        void batteryHelperUsesApplicationContext() throws IOException {
            String source = driver().readSourceCode();
            boolean usesAppContext = source.contains("BatteryHelper.level(context.getApplicationContext())")
                    || source.contains("BatteryHelper.level(appContext)");
            assertTrue(usesAppContext, "BatteryHelper must be called with application context for " + variantName());
        }
    }

    @Nested
    @DisplayName("Original")
    class Original extends CommonCases {
        @Override
        Driver driver() {
            return new Driver(BASE_PACKAGE + ".original" + TARGET_CLASS);
        }

        @Override
        String variantName() {
            return "original";
        }
    }

    @Nested
    @DisplayName("Misuse")
    class Misuse extends CommonCases {
        @Override
        Driver driver() {
            return new Driver(BASE_PACKAGE + ".misuse" + TARGET_CLASS);
        }

        @Override
        String variantName() {
            return "misuse";
        }
    }

    @Nested
    @DisplayName("Fixed")
    class Fixed extends CommonCases {
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
