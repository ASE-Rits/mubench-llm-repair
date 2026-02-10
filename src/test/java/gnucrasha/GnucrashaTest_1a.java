package gnucrasha._1a;

import gnucrasha._1a.mocks.GnuCashApplication;
import gnucrasha._1a.mocks.Intent;
import gnucrasha._1a.mocks.UxArgument;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class GnucrashaTest_1a {

    abstract static class CommonCases {

        abstract Driver driver();

        @Test
        public void startsLockScreenWithUid() {
            Intent result = driver().executeOnResume(true, false, "ACTION_VIEW", "UID-123");

            assertNotNull(result);
            assertEquals("UID-123", result.getStringExtra(UxArgument.SELECTED_ACCOUNT_UID));
        }
    }
    public static class Original extends CommonCases {

        @Override
        Driver driver() {
            return new Driver("gnucrasha._1a.original.PassLockActivity");
        }
    }
    public static class Misuse extends CommonCases {

        @Override
        Driver driver() {
            return new Driver("gnucrasha._1a.misuse.PassLockActivity");
        }
    }
    public static class Fixed extends CommonCases {

        @Override
        Driver driver() {
            return new Driver("gnucrasha._1a.fixed.PassLockActivity");
        }
    }
}
