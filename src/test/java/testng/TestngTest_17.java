package testng;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import testng._17.Driver;
import testng._17.mocks.MockTestContext;

import static org.junit.Assert.*;

/**
 * Test for TestNG Case 17 - JUnitXMLReporter m_configIssues synchronization bug.
 * 
 * Bug Type: missing/condition/synchronization
 * The bug is missing synchronized block when iterating over m_configIssues.
 * 
 * Tests include both static analysis and dynamic testing.
 */
@RunWith(Enclosed.class)
public class TestngTest_17 {
    
    abstract static class CommonCases {
        abstract Driver driver();
        
        // ========== Static Analysis Tests ==========

        @Test
        public void testSynchronizedBlockPresent() throws Exception {
            assertTrue("synchronized(m_configIssues) block should be present", driver().hasSynchronizedBlock());
        }
        
        @Test
        public void testCorrectlyFixed() throws Exception {
            assertTrue("Iteration should be inside synchronized block", driver().isCorrectlyFixed());
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
