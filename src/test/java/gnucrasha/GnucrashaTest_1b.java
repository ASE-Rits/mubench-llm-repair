package gnucrasha._1b;

import gnucrasha._1b.mocks.GnuCashApplication;
import gnucrasha._1b.mocks.Intent;
import gnucrasha._1b.mocks.UxArgument;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

class GnucrashaTest_1b {

    abstract static class CommonCases {

        abstract Driver driver();

        @Test
        public void successfulPasscodeReturnsToCaller() {
            Intent result = driver().submitPasscode("1234", "1234", "caller.Clazz", "ACTION", "UID-789");

            assertNotNull(result);
            assertEquals("caller.Clazz", result.getClassName());
            assertEquals("UID-789", result.getStringExtra(UxArgument.SELECTED_ACCOUNT_UID));
            assertTrue(GnuCashApplication.PASSCODE_SESSION_INIT_TIME > 0L);
        }

        @Test
        public void backPressNavigatesHome() {
            Intent home = driver().pressBack();

            assertNotNull(home);
            assertEquals(Intent.ACTION_MAIN, home.getAction());
            assertTrue(home.getCategories().contains(Intent.CATEGORY_HOME));
            assertEquals(Intent.FLAG_ACTIVITY_NEW_TASK, home.getFlags());
        }
    }
    public static class Original extends CommonCases {

        @Override
        Driver driver() {
            return new Driver("gnucrasha._1b.original.PasscodeLockScreenActivity");
        }
    }
    public static class Misuse extends CommonCases {

        @Override
        Driver driver() {
            return new Driver("gnucrasha._1b.misuse.PasscodeLockScreenActivity");
        }

        @Test
        public void losesAccountUid() {
            Intent result = driver().submitPasscode("1234", "1234", "caller.Clazz", "ACTION", "UID-789");

            assertNotNull(result);
            assertNull(result.getStringExtra(UxArgument.SELECTED_ACCOUNT_UID));
        }
    }
    public static class Fixed extends CommonCases {

        @Override
        Driver driver() {
            return new Driver("gnucrasha._1b.fixed.PasscodeLockScreenActivity");
        }
    }
}
