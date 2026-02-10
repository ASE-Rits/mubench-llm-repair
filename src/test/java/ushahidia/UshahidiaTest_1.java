package ushahidia;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import ushahidia._1.Driver;
import ushahidia._1.mocks.Cursor;
import ushahidia._1.requirements.IOpenGeoSmsSchema;

import static org.junit.Assert.*;

/**
 * Tests for OpenGeoSmsDao variants.
 * Bug: Cursor.close() not called in getReportState() method.
 */
class UshahidiaTest_1 {

    abstract static class CommonLogic {
        abstract Driver createDriver() throws Exception;

        @Test
        public void testCursorClosedWhenRecordsExist() throws Exception {
            Driver driver = createDriver();
            driver.setQueryResult(1, IOpenGeoSmsSchema.STATE_PENDING);

            int result = driver.getReportState(123L);

            Cursor cursor = driver.getLastCursor();
            assertNotNull("Cursor should have been created", cursor);
            assertTrue("Cursor should be closed after getReportState()", cursor.isClosed());
            assertEquals(IOpenGeoSmsSchema.STATE_PENDING, result);
        }

        @Test
        public void testCursorClosedWhenNoRecords() throws Exception {
            Driver driver = createDriver();
            driver.setQueryResult(0, 0);

            int result = driver.getReportState(456L);

            Cursor cursor = driver.getLastCursor();
            assertNotNull("Cursor should have been created", cursor);
            assertTrue("Cursor should be closed even when no records found", cursor.isClosed());
            assertEquals(IOpenGeoSmsSchema.STATE_NOT_OPENGEOSMS, result);
        }

        @Test
        public void testAddReport() throws Exception {
            Driver driver = createDriver();
            assertTrue(driver.addReport(100L));
        }

        @Test
        public void testSetReportState() throws Exception {
            Driver driver = createDriver();
            assertTrue(driver.setReportState(100L, IOpenGeoSmsSchema.STATE_PENDING));
            assertTrue(driver.setReportState(100L, IOpenGeoSmsSchema.STATE_SENT));
        }

        @Test
        public void testSetReportStateInvalidState() throws Exception {
            Driver driver = createDriver();
            assertFalse(driver.setReportState(100L, 999));
        }

        @Test
        public void testDeleteReport() throws Exception {
            Driver driver = createDriver();
            assertTrue(driver.deleteReport(100L));
        }

        @Test
        public void testDeleteReports() throws Exception {
            Driver driver = createDriver();
            assertTrue(driver.deleteReports());
        }
    }
    public static class Original extends CommonLogic {
        @Override
        Driver createDriver() throws Exception {
            return new Driver("original");
        }
    }

    // Misuseはバグがあるため必ず失敗（Cursor.close()が呼ばれない）
    public static class Misuse extends CommonLogic {
        @Override
        Driver createDriver() throws Exception {
            return new Driver("misuse");
        }
    }
    public static class Fixed extends CommonLogic {
        @Override
        Driver createDriver() throws Exception {
            return new Driver("fixed");
        }
    }
}
