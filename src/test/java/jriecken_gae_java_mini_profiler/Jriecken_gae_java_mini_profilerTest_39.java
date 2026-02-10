package jriecken_gae_java_mini_profiler;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import jriecken_gae_java_mini_profiler._39.Driver;

/**
 * Test class for jriecken-gae-java-mini-profiler case 39.
 * 
 * Bug: MiniProfilerAppstats.getAppstatsDataFor() calls Long.parseLong() 
 * without handling NumberFormatException.
 * 
 * - Original: Has try-catch for NumberFormatException (correct)
 * - Misuse: No exception handling (bug)
 * - Fixed: Has try-catch for NumberFormatException (correct)
 */
@RunWith(Enclosed.class)
public class Jriecken_gae_java_mini_profilerTest_39 {
    
    private static final String BASE_PACKAGE = "jriecken_gae_java_mini_profiler._39";
    
    /**
     * Common test logic for all variants.
     */
    abstract static class CommonCases {
        
        abstract Driver driver();
        
        /**
         * Test that invalid (non-numeric) appstatsId is handled gracefully
         * without throwing NumberFormatException.
         */
        @Test
        public void testHandlesInvalidIdGracefully() {
            Driver d = driver();
            
            // Test with various invalid inputs
            assertTrue("Should handle 'invalid' string without throwing NumberFormatException", d.handlesInvalidIdGracefully("invalid"));
            assertTrue("Should handle 'abc123' string without throwing NumberFormatException", d.handlesInvalidIdGracefully("abc123"));
            assertTrue("Should handle empty string without throwing NumberFormatException", d.handlesInvalidIdGracefully(""));
            assertTrue("Should handle decimal string without throwing NumberFormatException", d.handlesInvalidIdGracefully("12.34"));
        }
        
        /**
         * Test that valid numeric appstatsId is processed correctly.
         */
        @Test
        public void testHandlesValidIdCorrectly() {
            Driver d = driver();
            
            // Test with valid numeric ID
            assertTrue("Should process valid numeric ID correctly", d.handlesValidIdCorrectly("12345"));
            assertTrue("Should process '0' correctly", d.handlesValidIdCorrectly("0"));
            assertTrue("Should process negative number correctly", d.handlesValidIdCorrectly("-1"));
        }
    }
    public static class Original extends CommonCases {
        
        @Override
        Driver driver() {
            return new Driver(BASE_PACKAGE + ".original.MiniProfilerAppstats");
        }
    }
    
    // Misuse: バグがあるためテストは失敗する（コメントアウト）
    // NumberFormatException を catch していないため、invalid ID で例外がスローされる
    public static class Misuse extends CommonCases {
        
        @Override
        Driver driver() {
            return new Driver(BASE_PACKAGE + ".misuse.MiniProfilerAppstats");
        }
    }
    public static class Fixed extends CommonCases {
        
        @Override
        Driver driver() {
            return new Driver(BASE_PACKAGE + ".fixed.MiniProfilerAppstats");
        }
    }
}
