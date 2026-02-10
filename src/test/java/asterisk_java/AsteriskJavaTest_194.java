package asterisk_java._194;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

/**
 * Exercises RtcpReceivedEvent payload parsing via source code inspection.
 * The misuse is missing exception handling for NumberFormatException in setPt().
 */
@RunWith(Enclosed.class)
public class AsteriskJavaTest_194 {

    private static final String VALID_VALUE = "200(Sender Report)";

    abstract static class CommonCase {
        abstract String sourceFile();

        @Test
        public void handlesNumberFormatException() throws Exception {
            String content = Files.readString(Path.of("src/main/java", sourceFile()));
            // Find the setPt method and check if it catches NumberFormatException
            int setPtIdx = content.indexOf("void setPt(");
            assertTrue("setPt method should exist", setPtIdx >= 0);
            
            // Get the method body (find the next method or end of class)
            int methodEnd = content.indexOf("\n    public ", setPtIdx + 1);
            if (methodEnd < 0) methodEnd = content.length();
            String setPtMethod = content.substring(setPtIdx, methodEnd);
            
            // The fix is to catch NumberFormatException and provide a better message
            assertTrue("setPt should catch NumberFormatException to provide helpful error messages", setPtMethod.contains("catch") && setPtMethod.contains("NumberFormatException"));
        }
    }
    public static class Original extends CommonCase {
        @Override
        String sourceFile() {
            return "asterisk_java/_194/original/RtcpReceivedEvent.java";
        }
    }
    public static class Misuse extends CommonCase {
        @Override
        String sourceFile() {
            return "asterisk_java/_194/misuse/RtcpReceivedEvent.java";
        }
    }
    public static class Fixed extends CommonCase {
        @Override
        String sourceFile() {
            return "asterisk_java/_194/fixed/RtcpReceivedEvent.java";
        }
    }
}
