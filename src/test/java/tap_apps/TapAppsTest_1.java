package tap_apps;

import tap_apps._1.Driver;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Test for tap-apps case 1: Cipher.getInstance() usage.
 * 
 * Bug: Cipher.getInstance("Blowfish") returns unsafe default configuration.
 * Fix: Cipher.getInstance("Blowfish/ECB/NoPadding") with explicit mode and padding.
 * 
 * Note: misuse.java has the bug, original.java is the fixed version.
 */
@RunWith(Enclosed.class)
public class TapAppsTest_1 {
    
    private static final String BASE_PACKAGE = "tap_apps._1";
    private static final String TARGET_CLASS = ".NSMobileMessenger";
    
    // Blowfish requires 8-byte key minimum
    private static final byte[] TEST_KEY = "12345678".getBytes();
    private static final byte[] TEST_DATA = "Test message data".getBytes();
    
    abstract static class CommonCases {
        
        abstract Driver createDriver();
        abstract String variantName();
        
        @Test
        public void testCipherHasExplicitModeAndPadding() throws Exception {
            Driver driver = createDriver();
            assertTrue("Cipher.getInstance() should use explicit Algorithm/Mode/Padding format for " + variantName(), driver.hasExplicitModeAndPadding());
        }
        
        @Test
        public void testNoUnsafeCipherCalls() throws Exception {
            Driver driver = createDriver();
            assertFalse("Should not use Cipher.getInstance() with just algorithm name for " + variantName(), driver.hasUnsafeCipherCall());
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
