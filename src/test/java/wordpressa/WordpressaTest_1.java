package wordpressa;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import wordpressa._1.Driver;
import wordpressa._1.mocks.ListView;
import wordpressa._1.mocks.SimperiumUtils;

import static org.junit.Assert.*;

/**
 * Test for wordpressa case 1: NotificationsListFragment.restoreListScrollPosition()
 * 
 * Bug: Calls ListFragment.getListView() without checking that ListFragment.isAdded(),
 * which might lead to crashes if the view is not yet initialized.
 */
@RunWith(Enclosed.class)
public class WordpressaTest_1 {

    abstract static class CommonLogic {

        abstract Driver driver();

        @Before
        public void setUp() {
            SimperiumUtils.reset();
        }

        @Test
        public void checksIsAddedBeforeGetListView() {
            Driver d = driver();
            boolean hasCheck = d.checksIsAddedBeforeGetListView();
            
            assertTrue("Should check isAdded() before calling getListView()", hasCheck);
        }

        @Test
        public void doesNotSetSelectionWhenNotAdded() {
            Driver d = driver();
            boolean selectionSet = d.testRestoreScrollPosition(false, 2, 5);
            
            assertFalse("Selection should NOT be set when fragment is not added", selectionSet);
        }
    }
    public static class Original extends CommonLogic {

        @Override
        Driver driver() {
            return new Driver("original");
        }
    }
    public static class Fixed extends CommonLogic {

        @Override
        Driver driver() {
            return new Driver("fixed");
        }
    }
    public static class Misuse extends CommonLogic {

        @Override
        Driver driver() {
            return new Driver("misuse");
        }
    }
}
