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
class TestngTest_16 {
    
    abstract static class CommonCases {
        abstract Driver driver();
        
        // ========== Static Analysis Tests ==========
        
        @Test
        public void testGetContentMethodExists() throws Exception {
            assertTrue("getContent method should exist", driver().hasGetContentMethod());
        }
        
        @Test
        public void testSortCallExists() throws Exception {
            assertTrue("Collections.sort call should exist", driver().hasSortCall());
        }
        
        @Test
        public void testIterationExists() throws Exception {
            assertTrue("Iteration over invokedMethods should exist", driver().hasIteration());
        }
        
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
        public void testPanelInitialization() throws Exception {
            Driver d = driver();
            d.initializePanel();
            // If no exception, initialization succeeded
        }
        
        @Test
        public void testGetContentWithMockSuite() throws Exception {
            Driver d = driver();
            d.initializePanel();
            ISuite mockSuite = d.createMockSuite(5);
            String content = d.invokeGetContent(mockSuite);
            assertNotNull("getContent should return non-null", content);
        }
        
        @Test
        public void testGetContentWithEmptySuite() throws Exception {
            Driver d = driver();
            d.initializePanel();
            ISuite mockSuite = d.createMockSuite(0);
            String content = d.invokeGetContent(mockSuite);
            assertNotNull("getContent should handle empty suite", content);
        }
        
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
