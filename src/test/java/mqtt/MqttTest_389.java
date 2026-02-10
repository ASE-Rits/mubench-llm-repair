package mqtt;

import mqtt._389.Driver;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Test for MqttSubscribe variants (mqtt/389).
 * 
 * Bug: DataOutputStream wrapping ByteArrayOutputStream should be flushed
 * before calling toByteArray() on the underlying stream.
 * 
 * This test uses dynamic execution via Driver to verify correct behavior.
 */
class MqttTest_389 {

    private static final String[] TEST_TOPICS = {"topic/test", "another/topic"};
    private static final int[] TEST_QOS = {1, 2};

    abstract static class CommonLogic {
        abstract Driver createDriver() throws Exception;

        @Test
        public void testGetPayloadReturnsBytes() throws Exception {
            Driver d = createDriver();
            byte[] payload = d.getPayload();
            assertNotNull("Payload should not be null", payload);
            assertTrue("Payload should not be empty", payload.length > 0);
        }

        @Test
        public void testGetHeaderReturnsBytes() throws Exception {
            Driver d = createDriver();
            byte[] header = d.getHeader();
            assertNotNull("Header should not be null", header);
            assertTrue("Header should not be empty", header.length > 0);
        }

        @Test
        public void testMessageType() throws Exception {
            Driver d = createDriver();
            byte type = d.getType();
            assertEquals("Message type should be SUBSCRIBE (8)", 8, type);
        }

        @Test
        public void testIsRetryable() throws Exception {
            Driver d = createDriver();
            assertTrue("SUBSCRIBE messages should be retryable", d.isRetryable());
        }

        @Test
        public void testMessageId() throws Exception {
            Driver d = createDriver();
            d.setMessageId(12345);
            assertEquals("Message ID should match", 12345, d.getMessageId());
        }
    }
    public static class Original extends CommonLogic {
        @Override
        Driver createDriver() throws Exception {
            return new Driver("original", TEST_TOPICS, TEST_QOS);
        }
    }

    // Misuseは常にコメントアウト（バグがあるため必ず失敗）
    public static class Misuse extends CommonLogic {
        @Override
        Driver createDriver() throws Exception {
            return new Driver("misuse", TEST_TOPICS, TEST_QOS);
        }
    }
    public static class Fixed extends CommonLogic {
        @Override
        Driver createDriver() throws Exception {
            return new Driver("fixed", TEST_TOPICS, TEST_QOS);
        }
    }
}
