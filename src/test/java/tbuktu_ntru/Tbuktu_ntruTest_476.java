package tbuktu_ntru;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import tbuktu_ntru._476.Driver;

/**
 * 動的テスト: SignaturePublicKey.getEncoded() の動作検証
 * 
 * Bug: DataOutputStream.flush() が呼ばれていないため、
 * バッファされたデータがByteArrayOutputStreamに書き込まれない可能性がある
 */
@RunWith(Enclosed.class)
public class Tbuktu_ntruTest_476 {

    abstract static class CommonLogic {

        abstract Driver driver() throws Exception;

        @Test
        public void testGetEncodedCallsFlushOrClose() throws Exception {
            Driver d = driver();
            assertTrue("getEncoded() should call flush() or close() before toByteArray()", d.hasFlushOrCloseInGetEncoded());
        }
    }
    public static class Original extends CommonLogic {
        @Override
        Driver driver() throws Exception {
            byte[] testBytes = Driver.createMinimalTestBytes(107, 2048);
            return new Driver("original", testBytes);
        }
    }
    public static class Misuse extends CommonLogic {
        @Override
        Driver driver() throws Exception {
            byte[] testBytes = Driver.createMinimalTestBytes(107, 2048);
            return new Driver("misuse", testBytes);
        }
    }
    public static class Fixed extends CommonLogic {
        @Override
        Driver driver() throws Exception {
            byte[] testBytes = Driver.createMinimalTestBytes(107, 2048);
            return new Driver("fixed", testBytes);
        }
    }
}
