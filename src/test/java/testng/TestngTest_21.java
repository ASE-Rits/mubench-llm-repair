package testng;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import testng._21.Driver;

import static org.junit.Assert.*;

/**
 * Test for TestNG Case 21 - Model.java synchronization bug.
 * 
 * Bug Type: missing/condition/synchronization
 * The bug is missing synchronized block when iterating over suite.getResults().
 * 
 * Tests include both static analysis and dynamic testing.
 */
class TestngTest_21 {
    
    abstract static class CommonCases {
        abstract Driver driver();
        
        // ========== Static Analysis Tests ==========
        
        @Test
        public void testInitMethodExists() throws Exception {
            assertTrue("init() method should exist", driver().hasInitMethod());
        }
        
        @Test
        public void testGetResultsCallExists() throws Exception {
            assertTrue("getResults() call should exist", driver().hasGetResultsCall());
        }
        
        @Test
        public void testIterationExists() throws Exception {
            assertTrue("Iteration over results should exist", driver().hasIteration());
        }
        
        @Test
        public void testSynchronizedBlockPresent() throws Exception {
            assertTrue("synchronized(results) block should be present", driver().hasSynchronizedBlock());
        }
        
        @Test
        public void testCorrectlyFixed() throws Exception {
            assertTrue("Iteration should be inside synchronized block", driver().isCorrectlyFixed());
        }
        
        // ========== Dynamic Tests ==========
        
        @Test
        public void testModelInitialization() throws Exception {
            Driver d = driver();
            d.initializeModel();
            // If no exception, initialization succeeded
        }
        
        @Test
        public void testGetSuites() throws Exception {
            Driver d = driver();
            d.initializeModel();
            Object suites = d.getSuites();
            assertNotNull("getSuites should return non-null", suites);
        }
        
        @Test
        public void testGetAllFailedResults() throws Exception {
            Driver d = driver();
            d.initializeModel();
            Object results = d.getAllFailedResults();
            assertNotNull("getAllFailedResults should return non-null", results);
        }
        
        @Test
        public void testNonnullList() throws Exception {
            Driver d = driver();
            d.initializeModel();
            Object result = d.nonnullList(null);
            assertNotNull("nonnullList(null) should return empty list", result);
        }
    }
    public static class Original extends CommonCases {
        @Override
        Driver driver() {
            return new Driver("original");
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
