package cego._1;

import cego._1.mocks.Intent;
import cego._1.mocks.Log;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class CegoTest_1 {

    abstract static class CommonCases {

        abstract Driver driver();

        abstract String expectedMime();

        @Before
        public void resetLog() {
            Log.reset();
        }

        @Test
        public void viewImageUsesExpectedMime() {
            Intent intent = driver().openBitmap("sample-bitmap");

            assertNotNull(intent);
            assertEquals(Intent.ACTION_VIEW, intent.getAction());
            assertNotNull(intent.getData());
            assertEquals(expectedMime(), intent.getType());
            assertNull(Log.getLastError());
        }
    }
    public static class Original extends CommonCases {

        @Override
        Driver driver() {
            return new Driver("cego._1.original.cgeoimages");
        }

        @Override
        String expectedMime() {
            return "image/jpeg";
        }
    }
    public static class Misuse extends CommonCases {

        @Override
        Driver driver() {
            return new Driver("cego._1.misuse.cgeoimages");
        }

        @Override
        String expectedMime() {
            return "image/jpeg";
        }
    }
    public static class Fixed extends CommonCases {

        @Override
        Driver driver() {
            return new Driver("cego._1.fixed.cgeoimages");
        }

        @Override
        String expectedMime() {
            return "image/jpeg";
        }
    }
}
