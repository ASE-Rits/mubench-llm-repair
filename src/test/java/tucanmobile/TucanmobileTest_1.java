package tucanmobile;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import tucanmobile._1.Driver;
import tucanmobile._1.mocks.ProgressDialog;
import tucanmobile._1.requirements.AnswerObject;

import static org.junit.Assert.*;

/**
 * Test for tucanmobile case 1: Dialog.dismiss() without checking isShowing().
 */
@RunWith(Enclosed.class)
public class TucanmobileTest_1 {

    private static final String BASE_PACKAGE = "tucanmobile._1";

    abstract static class CommonLogic {
        abstract Driver createDriver();

        @Test
        public void testDismissWhenDialogIsNotShowing() {
            Driver driver = createDriver();
            
            // onPreExecute creates the dialog
            driver.invokeOnPreExecute();
            
            ProgressDialog dialog = driver.getDialog();
            assertNotNull("Dialog should be created in onPreExecute", dialog);
            
            // Simulate dialog already dismissed (not showing)
            dialog.setShowing(false);
            assertFalse("Dialog should not be showing", dialog.isShowing());
            
            // onPostExecute should not throw when dialog is not showing
            AnswerObject result = new AnswerObject("", "", null, null);
            try {

                driver.invokeOnPostExecute(result);

            } catch (Throwable t) {

                fail("Should not throw when dismissing a dialog that is not showing" + ": " + t.getMessage());

            }
        }
    }
    public static class Original extends CommonLogic {
        @Override
        Driver createDriver() {
            return new Driver(BASE_PACKAGE + ".original.SimpleSecureBrowser");
        }
    }

    // Misuseは常にコメントアウト（バグがあるため必ず失敗）
    public static class Misuse extends CommonLogic {
        @Override
        Driver createDriver() {
            return new Driver(BASE_PACKAGE + ".misuse.SimpleSecureBrowser");
        }
    }
    public static class Fixed extends CommonLogic {
        @Override
        Driver createDriver() {
            return new Driver(BASE_PACKAGE + ".fixed.SimpleSecureBrowser");
        }
    }
}
