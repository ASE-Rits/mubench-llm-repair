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
@RunWith(Enclosed.class)
public class UshahidiaTest_1 {

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
