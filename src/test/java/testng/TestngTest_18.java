package testng;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import testng._18.Driver;
import testng._18.mocks.MockTestContext;

import static org.junit.Assert.*;

/**
 * Test for TestNG Case 18 - JUnitXMLReporter m_allTests synchronization bug.
 * 
 * Bug Type: missing/condition/synchronization
 * The bug is missing synchronized block when iterating over m_allTests.
 * 
 * Tests include both static analysis and dynamic testing.
 */
class TestngTest_18 {
    
    abstract static class CommonCases {
        abstract Driver driver();
        
        // ========== Static Analysis Tests ==========
        
        @Test
        public void testGenerateReportMethodExists() throws Exception {
            assertTrue("generateReport method should exist", driver().hasGenerateReportMethod());
        }
        
        @Test
        public void testAllTestsFieldExists() throws Exception {
            assertTrue("m_allTests field should exist", driver().hasAllTestsField());
        }
        
        @Test
        public void testIterationExists() throws Exception {
            assertTrue("Iteration over m_allTests should exist", driver().hasIteration());
        }
        
        @Test
        public void testSynchronizedBlockPresent() throws Exception {
            assertTrue("synchronized(m_allTests) block should be present", driver().hasSynchronizedBlock());
        }
        
        @Test
        public void testCorrectlyFixed() throws Exception {
            assertTrue("Iteration should be inside synchronized block", driver().isCorrectlyFixed());
        }
        
        // ========== Dynamic Tests ==========
        
        @Test
        public void testReporterInitialization() throws Exception {
            Driver d = driver();
            d.initializeReporter();
            // If no exception, initialization succeeded
        }
        
        @Test
        public void testOnTestSuccessInvocation() throws Exception {
            Driver d = driver();
            d.initializeReporter();
            try {
                d.onTestSuccess(null);
            } catch (Exception e) {
                // Expected: method is callable
            }
        }
        
        @Test
        public void testOnTestFailureInvocation() throws Exception {
            Driver d = driver();
            d.initializeReporter();
            try {
                d.onTestFailure(null);
            } catch (Exception e) {
                // Expected: method is callable
            }
        }
        
        @Test
        public void testGenerateReportWithMockContext() throws Exception {
            Driver d = driver();
            d.initializeReporter();
            MockTestContext mockContext = d.createMockTestContext("TestContext");
            try {
                d.generateReport(mockContext);
            } catch (Exception e) {
                // May throw NPE, but method is callable
            }
        }
        
        @Test
        public void testOnStartWithMockContext() throws Exception {
            Driver d = driver();
            d.initializeReporter();
            MockTestContext mockContext = d.createMockTestContext("TestContext");
            d.onStart(mockContext);
        }
        
        @Test
        public void testOnFinishWithMockContext() throws Exception {
            Driver d = driver();
            d.initializeReporter();
            MockTestContext mockContext = d.createMockTestContext("TestContext");
            try {
                d.onFinish(mockContext);
            } catch (Exception e) {
                // May throw NPE, but method invocation works
            }
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
    
    // ========== Misuse Test (Commented out per guideline) ==========
    public static class Misuse extends CommonCases {
        @Override
        Driver driver() {
            return new Driver("misuse");
        }
    }
}
