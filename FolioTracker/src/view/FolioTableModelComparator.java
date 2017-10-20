package view;

import java.util.Comparator;

public class FolioTableModelComparator implements Comparator<Object> {

	@Override
	public int compare(Object o1, Object o2) {
		// For number columns, sort numbers by size, but sort strings
		// alphabetically.
		// This is to avoid runtime exception when comparing numbers to
		// "N/A".
		if (o1 instanceof String) {
			if (o2 instanceof String)
				return ((String) o1).compareTo((String) o2);
			return -1; // Strings come before numbers
		} else if (o2 instanceof String) {
			return 1;
		} else if (o1 instanceof Long && o2 instanceof Long) {
			return ((Long) o1).compareTo((Long) o2);
		} else if (o1 instanceof Double && o2 instanceof Double) {
			return ((Double) o1).compareTo((Double) o2);
		} else {
			return 0; // This should never happen - all entries are
						// either Strings, Longs, or Doubles.
		}
	}

}
