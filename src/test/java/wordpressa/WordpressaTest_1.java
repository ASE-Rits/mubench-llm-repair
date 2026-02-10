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
class WordpressaTest_1 {

    abstract static class CommonLogic {

        abstract Driver driver();

        @Before
        void setUp() {
            SimperiumUtils.reset();
        }

        @Test
        public void restoresScrollPositionWhenAdded() {
            Driver d = driver();
            boolean selectionSet = d.testRestoreScrollPosition(true, 2, 5);
            
            assertTrue("Selection should be set when fragment is added", selectionSet);
        }

        @Test
        public void doesNotRestoreWhenInvalidPosition() {
            Driver d = driver();
            boolean selectionSet = d.testRestoreScrollPosition(true, ListView.INVALID_POSITION, 5);
            
            assertFalse("Selection should not be set when position is INVALID_POSITION", selectionSet);
        }

        @Test
        public void doesNotRestoreWhenPositionExceedsCount() {
            Driver d = driver();
            boolean selectionSet = d.testRestoreScrollPosition(true, 10, 5);
            
            assertFalse("Selection should not be set when position exceeds count", selectionSet);
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
