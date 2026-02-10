package screen_notifications;

import screen_notifications._1.Driver;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

class ScreenNotificationsTest_1 {

    private static final String BASE_PACKAGE = "screen_notifications._1";
    private static final String TARGET_CLASS = ".AppsActivity";

    abstract static class CommonCases {

        abstract Driver createDriver();
        abstract String variantName();

        @Test
        public void loadInBackgroundReturnsData() {
            Driver driver = createDriver();
            driver.addApp("com.test.app1", "Alpha App", false);
            driver.addApp("com.test.app2", "Beta App", false);

            Object data = driver.loadInBackground(0);
            assertNotNull("Data should not be null for " + variantName(), data);
        }

        @Test
        public void loadInBackgroundHandlesOOM() {
            Driver driver = createDriver();
            driver.addApp("com.test.oom", "OOM App", true);  // This will throw OOM

            // Should not throw OutOfMemoryError
            try {
                driver.loadInBackground(0);
            } catch (OutOfMemoryError e) {
                fail("Should handle OutOfMemoryError gracefully for " + variantName() + ": " + e.getMessage());
            }
        }
    }
    public static class Original extends CommonCases {
        @Override
        Driver createDriver() {
            return new Driver(BASE_PACKAGE + ".original" + TARGET_CLASS);
        }

        @Override
        String variantName() {
            return "original";
        }
    }
    public static class Misuse extends CommonCases {
        @Override
        Driver createDriver() {
            return new Driver(BASE_PACKAGE + ".misuse" + TARGET_CLASS);
        }

        @Override
        String variantName() {
            return "misuse";
        }
    }
    public static class Fixed extends CommonCases {
        @Override
        Driver createDriver() {
            return new Driver(BASE_PACKAGE + ".fixed" + TARGET_CLASS);
        }

        @Override
        String variantName() {
            return "fixed";
        }
    }
}
