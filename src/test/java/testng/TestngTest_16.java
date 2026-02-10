package testng;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import testng._16.Driver;
import testng._16.requirements.org.testng.ISuite;

import static org.junit.Assert.*;

/**
 * Test for TestNG Case 16 - ChronologicalPanel synchronization bug.
 * 
 * Bug Type: missing/condition/synchronization
 * The bug is missing synchronized block when iterating over a synchronized collection.
 * 
 * Tests include both:
 * - Static analysis: verify synchronized block in source code
 * - Dynamic testing: instantiate panel and invoke methods
 */
@RunWith(Enclosed.class)
public class TestngTest_16 {
    
    abstract static class CommonCases {
        abstract Driver driver();
        
        // ========== Static Analysis Tests ==========

        @Test
        public void testSynchronizedBlockPresent() throws Exception {
            assertTrue("synchronized(invokedMethods) block should be present", driver().hasSynchronizedBlock());
        }
        
        @Test
        public void testCorrectlyFixed() throws Exception {
            assertTrue("Sort and iteration should be inside synchronized block", driver().isCorrectlyFixed());
        }
        
        // ========== Dynamic Tests ==========
        
        @Test
        public void testConcurrentAccess() throws Exception {
            Driver d = driver();
            d.initializePanel();
            ISuite mockSuite = d.createMockSuite(10);
            boolean success = d.testConcurrentAccess(mockSuite, 4);
            assertTrue("Concurrent access should not cause ConcurrentModificationException", success);
        }
    }
    public static class Original extends CommonCases {
        @Override
        Driver driver() {
            return new Driver("original");
        }
        
        // Original は不完全な修正版のため、このテストは失敗が期待動作
        @Override
        @Test
        @Ignore
        public void testCorrectlyFixed() throws Exception {
        }
    }
    public static class Fixed extends CommonCases {
        @Override
        Driver driver() {
            return new Driver("fixed");
        }
    }
    public static class Misuse extends CommonCases {
        @Override
        Driver driver() {
            return new Driver("misuse");
        }
    }
}
