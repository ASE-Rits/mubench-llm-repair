package tbuktu_ntru;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import tbuktu_ntru._473.Driver;
import java.io.ByteArrayOutputStream;

/**
 * 動的テスト: EncryptionParameters.writeTo(OutputStream) の動作検証
 * 
 * Bug: DataOutputStream.flush() が呼ばれていないため、
 * バッファされたデータがOutputStreamに書き込まれない可能性がある
 */
@RunWith(Enclosed.class)
public class Tbuktu_ntruTest_473 {

    abstract static class CommonLogic {

        abstract Driver driver() throws Exception;

        @Test
        public void testWriteToOutputsData() throws Exception {
            Driver d = driver();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            d.writeTo(baos);
            byte[] data = baos.toByteArray();
            
            assertNotNull(data);
            assertTrue("writeTo should write data to the output stream", data.length > 0);
        }

        @Test
        public void testWriteToConsistentLength() throws Exception {
            Driver d = driver();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            d.writeTo(baos);
            int length = baos.toByteArray().length;
            
            assertTrue("writeTo should write at least 57 bytes, but wrote " + length, length >= 57);
        }

        @Test
        public void testWriteToComplete() throws Exception {
            Driver d = driver();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            d.writeTo(baos);
            byte[] data = baos.toByteArray();
            
            // EES1087EP2 パラメータセットの場合:
            // N=1087 = 0x0000043F
            assertEquals("First byte of N should be 0x00", 0x00, data[0] & 0xFF);
            assertEquals("Second byte of N should be 0x00", 0x00, data[1] & 0xFF);
            assertEquals("Third byte of N should be 0x04", 0x04, data[2] & 0xFF);
            assertEquals("Fourth byte of N should be 0x3F", 0x3F, data[3] & 0xFF);
            
            // q=2048 = 0x00000800
            assertEquals("First byte of q should be 0x00", 0x00, data[4] & 0xFF);
            assertEquals("Second byte of q should be 0x00", 0x00, data[5] & 0xFF);
            assertEquals("Third byte of q should be 0x08", 0x08, data[6] & 0xFF);
            assertEquals("Fourth byte of q should be 0x00", 0x00, data[7] & 0xFF);
        }

        @Test
        public void testWriteToFlushesBuffer() throws Exception {
            Driver d = driver();
            int bytesWritten = d.writeToBuffered();
            
            assertTrue("writeTo should flush the buffer - expected > 0 bytes but got " + bytesWritten, bytesWritten > 0);
        }
    }
    public static class Original extends CommonLogic {
        @Override
        Driver driver() throws Exception {
            return new Driver("original");
        }
    }
    public static class Misuse extends CommonLogic {
        @Override
        Driver driver() throws Exception {
            return new Driver("misuse");
        }
    }
    public static class Fixed extends CommonLogic {
        @Override
        Driver driver() throws Exception {
            return new Driver("fixed");
        }
    }
}
