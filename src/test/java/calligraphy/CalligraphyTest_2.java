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

class CalligraphyTest_2 {

    abstract static class CommonCases {

        abstract Driver driver();

        abstract String expectedStyleFallback();

        abstract String expectedThemeFallback();

        @Test
        public void applyFontWithTypeface() {
            Driver driver = driver();
            TextView textView = new TextView();
            Typeface typeface = new Typeface("fonts/Roboto-Regular.ttf");

            boolean applied = driver.applyFont(textView, typeface);

            assertTrue(applied);
            assertSame(typeface, textView.getTypeface());
            assertEquals(Paint.SUBPIXEL_TEXT_FLAG, textView.getPaintFlags());
        }

        @Test
        public void applyFontHonorsExplicitFont() {
            Driver driver = driver();
            Context context = new Context();
            TextView textView = new TextView();
            CalligraphyConfig config = new CalligraphyConfig("config-font.ttf");

            driver.applyFont(context, textView, config, "view-font.ttf");

            assertNotNull(textView.getTypeface());
            assertEquals("view-font.ttf", textView.getTypeface().getPath());
            assertEquals(Paint.SUBPIXEL_TEXT_FLAG, textView.getPaintFlags());
        }

        @Test
        public void applyFontFallsBackToConfig() {
            Driver driver = driver();
            Context context = new Context();
            TextView textView = new TextView();
            CalligraphyConfig config = new CalligraphyConfig("config-font.ttf");

            driver.applyFont(context, textView, config, "");

            assertNotNull(textView.getTypeface());
            assertEquals("config-font.ttf", textView.getTypeface().getPath());
            assertEquals(Paint.SUBPIXEL_TEXT_FLAG, textView.getPaintFlags());
        }

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

        @Test
        public void pullFontPathFromThemeMissingStyleFails() {
            InvocationResult result = driver().pullFontPathFromThemeWithMissingStyle();

            assertFalse(result.isSuccess());
            assertNotNull(result.getError());
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

        @Test
        public void pullFontPathFromThemeMissingStyleSucceeds() {
            InvocationResult result = driver().pullFontPathFromThemeWithMissingStyle();

            assertTrue(result.isSuccess());
            assertNull(result.getValue());
        }
    }
}
