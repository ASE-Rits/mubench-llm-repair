package testng;

import org.junit.Test;
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
 * Dynamic testing: instantiate panel and invoke methods concurrently to detect
 * ConcurrentModificationException.
 */
@RunWith(Enclosed.class)
public class TestngTest_16 {
    
    abstract static class CommonCases {
        abstract Driver driver();
        
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
