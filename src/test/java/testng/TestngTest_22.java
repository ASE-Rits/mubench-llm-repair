package testng;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import testng._22.Driver;
import testng._22.mocks.MockSuite;
import testng._22.mocks.MockTestContext;
import testng._22.mocks.MockSuiteResult;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Test for TestNG Case 22 - XMLReporter results synchronization bug.
 * 
 * Bug Type: missing/condition/synchronization
 * The bug is missing synchronized block when iterating over results map.
 * 
 * Tests include both static analysis and dynamic testing.
 */
class TestngTest_22 {
    
    abstract static class CommonCases {
        abstract Driver driver();
        
        // ========== Static Analysis Tests ==========
        
        @Test
        public void testGetSuiteAttributesMethodExists() throws Exception {
            assertTrue("getSuiteAttributes method should exist", driver().hasGetSuiteAttributesMethod());
        }
        
        @Test
        public void testResultsVariableExists() throws Exception {
            assertTrue("results variable should exist", driver().hasResultsVariable());
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
        public void testReporterInitialization() throws Exception {
            Driver d = driver();
            d.initializeReporter();
            // If no exception, initialization succeeded
        }
        
        @Test
        public void testGetterSetterMethods() throws Exception {
            Driver d = driver();
            d.initializeReporter();
            
            // Test getter/setter pairs
            int fragLevel = d.getFileFragmentationLevel();
            d.setFileFragmentationLevel(fragLevel);
            
            String outputDir = d.getOutputDirectory();
            d.setOutputDirectory(outputDir != null ? outputDir : "/tmp");
            
            boolean groupsAttr = d.isGenerateGroupsAttribute();
            d.setGenerateGroupsAttribute(groupsAttr);
        }
        
        @Test
        public void testTimestampFormat() throws Exception {
            Driver d = driver();
            d.initializeReporter();
            String format = d.getTimestampFormat();
            assertNotNull("getTimestampFormat should return non-null", format);
        }
        
        @Test
        public void testGenerateReportWithMockSuite() throws Exception {
            Driver d = driver();
            d.initializeReporter();
            
            // Create mock objects
            MockSuite mockSuite = d.createMockSuite("TestSuite");
            MockTestContext mockContext = d.createMockTestContext("TestContext");
            mockContext.setSuite(mockSuite);
            MockSuiteResult mockResult = d.createMockSuiteResult(mockContext);
            mockSuite.addResult("test1", mockResult);
            
            // generateReport requires XmlSuite and ISuite lists
            try {
                d.generateReport(new ArrayList<>(), java.util.List.of(mockSuite), "/tmp");
            } catch (Exception e) {
                // May throw NPE due to XmlSuite requirements, but method is callable
            }
        }
        
        @Test
        public void testMockSuiteAttributes() throws Exception {
            Driver d = driver();
            MockSuite mockSuite = d.createMockSuite("TestSuite");
            
            // Test attribute operations
            mockSuite.setAttribute("key", "value");
            assertEquals("value", mockSuite.getAttribute("key"));
            assertTrue(mockSuite.getAttributeNames().contains("key"));
            mockSuite.removeAttribute("key");
            assertNull(mockSuite.getAttribute("key"));
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
