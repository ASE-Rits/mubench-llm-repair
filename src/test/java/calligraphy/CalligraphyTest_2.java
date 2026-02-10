package calligraphy._2;

import calligraphy._2.Driver.InvocationResult;
import calligraphy._2.mocks.CalligraphyConfig;
import calligraphy._2.mocks.Context;
import calligraphy._2.mocks.Paint;
import calligraphy._2.mocks.TextView;
import calligraphy._2.mocks.Typeface;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class CalligraphyTest_2 {

    abstract static class CommonCases {

        abstract Driver driver();

        abstract String expectedStyleFallback();

        abstract String expectedThemeFallback();

        @Test
        public void pullFontPathFromStyleHandlesRuntime() {
            Driver driver = driver();

            InvocationResult result = driver.pullFontPathFromStyleWithFallback("style-fallback.ttf");

            assertTrue(result.isSuccess());
            assertEquals(expectedStyleFallback(), result.getValue());
        }

        @Test
        public void pullFontPathFromThemeHandlesRuntime() {
            Driver driver = driver();

            InvocationResult result = driver.pullFontPathFromThemeWithFallback("theme-fallback.ttf");

            assertTrue(result.isSuccess());
            assertEquals(expectedThemeFallback(), result.getValue());
        }
    }
    public static class Original extends CommonCases {

        @Override
        Driver driver() {
            return new Driver("calligraphy._2.original.CalligraphyUtils");
        }

        @Override
        String expectedStyleFallback() {
            return null;
        }

        @Override
        String expectedThemeFallback() {
            return null;
        }
    }
    public static class Misuse extends CommonCases {

        @Override
        Driver driver() {
            return new Driver("calligraphy._2.misuse.CalligraphyUtils");
        }

        @Override
        String expectedStyleFallback() {
            return null;
        }

        @Override
        String expectedThemeFallback() {
            return null;
        }
    }
    public static class Fixed extends CommonCases {

        @Override
        Driver driver() {
            return new Driver("calligraphy._2.fixed.CalligraphyUtils");
        }

        @Override
        String expectedStyleFallback() {
            return "style-fallback.ttf";
        }

        @Override
        String expectedThemeFallback() {
            return "theme-fallback.ttf";
        }
    }
}
