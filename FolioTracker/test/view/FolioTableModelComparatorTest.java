package view;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FolioTableModelComparatorTest {

    @Test
    public void compareTwoStringsTest() {
        FolioTableModelComparator comparator = new FolioTableModelComparator();
        assertTrue(comparator.compare("aardvark", "zebra") < 0);
        assertTrue(comparator.compare("zebra", "aardvark") > 0);
        assertEquals(0, comparator.compare("zebra", "zebra"));
    }

    @Test
    public void compareTwoLongs() {
        FolioTableModelComparator comparator = new FolioTableModelComparator();
        assertTrue(comparator.compare(0L, 1L) < 0);
        assertTrue(comparator.compare(100L, -31L) > 0);
        assertEquals(0, comparator.compare(2L, 2L));
    }

    @Test
    public void compareTwoDoubles() {
        FolioTableModelComparator comparator = new FolioTableModelComparator();
        assertTrue(comparator.compare(0.3, 1.2) < 0);
        assertTrue(comparator.compare(10.63, -3.111) > 0);
        assertEquals(0, comparator.compare(2.0, 2.0));
    }

    @Test
    public void compareStringWithLong() {
        FolioTableModelComparator comparator = new FolioTableModelComparator();
        assertTrue(comparator.compare("aardvark", 1.2) < 0);
        assertTrue(comparator.compare(10.63, "aardvark") > 0);
    }

}