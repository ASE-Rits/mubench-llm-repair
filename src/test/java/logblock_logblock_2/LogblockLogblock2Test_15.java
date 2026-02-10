package logblock_logblock_2;

import logblock_logblock_2._15.Driver;
import logblock_logblock_2._15.requirements.entry.blob.PaintingBlob;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Test for logblock-logblock-2 case 15.
 * 
 * Bug: DataOutputStream wrapping ByteArrayOutputStream should be flushed/closed
 * before calling toByteArray() on the underlying ByteArrayOutputStream.
 * 
 * This test uses dynamic execution via Driver to verify correct behavior.
 */
class LogblockLogblock2Test_15 {

    abstract static class CommonLogic {
        abstract Driver createDriver() throws Exception;

        @Test
        public void testPaintingBlobRoundTrip() throws Exception {
            Driver d = createDriver();
            
            // Create a PaintingBlob with test data
            PaintingBlob original = d.createPaintingBlob(1);
            original.setArt("artistic");
            original.setDirection((byte) 5);
            
            // Write to bytes and read back
            byte[] bytes = d.writeBlobToBytes(original);
            assertNotNull("Written bytes should not be null", bytes);
            assertTrue("Written bytes should not be empty", bytes.length > 0);
            
            // Read back and verify
            PaintingBlob restored = d.readBlobFromBytes(bytes);
            assertEquals("Art should match after round trip", original.getArt(), restored.getArt());
            assertEquals("Direction should match after round trip", original.getDirection(), restored.getDirection());
        }

        @Test
        public void testPaintingTestExecution() throws Exception {
            Driver d = createDriver();
            // This will throw an exception if the DataOutputStream is not properly flushed/closed
            try {

                d.paintingTest();

            } catch (Throwable t) {

                fail("paintingTest should complete without errors" + ": " + t.getMessage());

            }
        }
    }
    public static class Original extends CommonLogic {
        @Override
        Driver createDriver() throws Exception {
            return new Driver("original");
        }
    }

    // Misuseは常にコメントアウト（バグがあるため必ず失敗）
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
