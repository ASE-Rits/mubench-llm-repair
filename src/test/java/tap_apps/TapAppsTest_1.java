package tap_apps;

import tap_apps._1.Driver;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for tap-apps case 1: Cipher.getInstance() usage.
 * 
 * Bug: Cipher.getInstance("Blowfish") returns unsafe default configuration.
 * Fix: Cipher.getInstance("Blowfish/ECB/NoPadding") with explicit mode and padding.
 * 
 * Note: misuse.java has the bug, original.java is the fixed version.
 */
class TapAppsTest_1 {
    
    private static final String BASE_PACKAGE = "tap_apps._1";
    private static final String TARGET_CLASS = ".NSMobileMessenger";
    
    // Blowfish requires 8-byte key minimum
    private static final byte[] TEST_KEY = "12345678".getBytes();
    private static final byte[] TEST_DATA = "Test message data".getBytes();
    
    abstract static class CommonCases {
        
        abstract Driver createDriver();
        abstract String variantName();
        
        @Test
        public void testDriverCreation() {
            Driver driver = createDriver();
            assertNotNull("Driver should be created for " + variantName(), driver);
        }
        
        @Test
        public void testGetPriority() throws Exception {
            Driver driver = createDriver();
            assertEquals("getPriority should return 0 for " + variantName(), (byte) 0, driver.getPriority());
        }
        
        @Test
        public void testPadArray8() throws Exception {
            Driver driver = createDriver();
            byte[] input = new byte[5];
            byte[] padded = driver.padArray8(input);
            assertEquals("padArray8 should pad 5 bytes to 8 for " + variantName(), 8, padded.length);
            
            byte[] input16 = new byte[16];
            byte[] padded16 = driver.padArray8(input16);
            assertEquals("padArray8 should not pad 16 bytes for " + variantName(), 16, padded16.length);
        }
        
        @Test
        public void testTestProperty() throws Exception {
            Driver driver = createDriver();
            assertTrue("testProperty should return true for valid string for " + variantName(), driver.testProperty("valid"));
            assertFalse("testProperty should return false for null for " + variantName(), driver.testProperty(null));
            assertFalse("testProperty should return false for empty string for " + variantName(), driver.testProperty(""));
        }
        
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
        
        @Test
        public void testRoundTrip() throws Exception {
            Driver driver = createDriver();
            assertTrue("Round-trip encryption/decryption should preserve data for " + variantName(), driver.roundTripTest(TEST_KEY, TEST_DATA));
        }
        
        @Test
        public void testEncryptionProducesDifferentOutput() throws Exception {
            Driver driver = createDriver();
            assertTrue("Encryption should produce different output for " + variantName(), driver.encryptionProducesDifferentOutput(TEST_KEY, TEST_DATA));
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
