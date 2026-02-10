package tbuktu_ntru;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import tbuktu_ntru._474.Driver;
import java.io.ByteArrayOutputStream;

/**
 * 動的テスト: SignatureParameters.writeTo(OutputStream) の動作検証
 * 
 * Bug: DataOutputStream.flush() が呼ばれていないため、
 * バッファされたデータがOutputStreamに書き込まれない可能性がある
 */
@RunWith(Enclosed.class)
public class Tbuktu_ntruTest_474 {

    abstract static class CommonLogic {

        abstract Driver driver() throws Exception;

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
