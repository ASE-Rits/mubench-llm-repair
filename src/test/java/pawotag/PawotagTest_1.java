package pawotag;

import pawotag._1.Driver;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Test for pawotag/1 - CryptoUtil empty array handling bug.
 * 
 * Bug: CryptoUtil.encrypt() calls Cipher.doFinal(byte[], int, int, byte[], int)
 * with a zero-length byte array, which causes issues on some JVMs (IBM JVM 6).
 * 
 * Fix: Check if inbytes.length == 0 and use a different doFinal() variant.
 */
@RunWith(Enclosed.class)
public class PawotagTest_1 {

    private static final String BASE_PACKAGE = "pawotag._1";

    abstract static class CommonCases {

        abstract Driver driver();

        @Before
        public void resetKey() {
            driver().resetSecretKey();
        }

        @Test
        public void encryptDecryptNonEmpty() {
            String original = "Hello, World!";
            String encrypted = driver().encrypt(original);
            assertNotNull(encrypted);
            assertNotEquals(original, encrypted);
            
            String decrypted = driver().decrypt(encrypted);
            assertEquals(original, decrypted);
        }

        @Test
        public void encryptDecryptEmpty() {
            String original = "";
            String encrypted = driver().encrypt(original);
            assertNotNull(encrypted);
            
            String decrypted = driver().decrypt(encrypted);
            assertEquals(original, decrypted);
        }

        @Test
        public void encryptNullInput() {
            String encrypted = driver().encrypt(null);
            assertNotNull(encrypted);
            
            String decrypted = driver().decrypt(encrypted);
            assertEquals("", decrypted);
        }

        @Test
        public void decryptNullInput() {
            String result = driver().decrypt(null);
            assertNull(result);
        }

        @Test
        public void hasEmptyArrayCheckPattern() throws Exception {
            assertTrue("encrypt() should check for empty input array before calling doFinal", driver().hasEmptyArrayCheck());
        }
    }
    public static class Original extends CommonCases {

        @Override
        Driver driver() {
            return new Driver(BASE_PACKAGE + ".original.CryptoUtil");
        }
    }

    // Misuse fails the hasEmptyArrayCheckPattern test because it doesn't check for empty arrays
    public static class Misuse extends CommonCases {
        @Override
        Driver driver() {
            return new Driver(BASE_PACKAGE + ".misuse.CryptoUtil");
        }
    }
    public static class Fixed extends CommonCases {

        @Override
        Driver driver() {
            return new Driver(BASE_PACKAGE + ".fixed.CryptoUtil");
        }
    }
}
