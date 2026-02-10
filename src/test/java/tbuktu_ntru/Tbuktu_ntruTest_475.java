package tbuktu_ntru;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import tbuktu_ntru._475.Driver;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

/**
 * 動的テスト: SignaturePrivateKey.getEncoded() の動作検証
 * 
 * Bug: DataOutputStream.flush() が呼ばれていないため、
 * バッファされたデータがByteArrayOutputStreamに書き込まれない可能性がある
 */
@RunWith(Enclosed.class)
public class Tbuktu_ntruTest_475 {

    /**
     * テスト用のシンプルな SignaturePrivateKey バイナリデータを作成
     */
    private static byte[] createSimpleTestBytes() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        
        int N = 107;
        int q = 2048;
        dos.writeShort(N);
        dos.writeShort(q);
        
        int flags = 0;
        dos.writeByte(flags);
        dos.writeFloat(1000.0f);
        dos.writeByte(1);
        
        int binary3TightBytes = (int) Math.ceil(N * 2.0 / 8);
        byte[] fBytes = new byte[binary3TightBytes];
        dos.write(fBytes);
        
        int fPrimeBytes = (int) Math.ceil(N * 11.0 / 8);
        byte[] fPrimeData = new byte[fPrimeBytes];
        dos.write(fPrimeData);
        
        dos.flush();
        return bos.toByteArray();
    }

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
            return new Driver("original", createSimpleTestBytes());
        }
    }
    public static class Misuse extends CommonLogic {
        @Override
        Driver driver() throws Exception {
            return new Driver("misuse", createSimpleTestBytes());
        }
    }
    public static class Fixed extends CommonLogic {
        @Override
        Driver driver() throws Exception {
            return new Driver("fixed", createSimpleTestBytes());
        }
    }
}
