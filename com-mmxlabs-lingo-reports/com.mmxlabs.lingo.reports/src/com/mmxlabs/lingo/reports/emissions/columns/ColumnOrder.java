package com.mmxlabs.lingo.reports.emissions.columns;

/**
 * Used for structuring the order of columns for the C02 emission reports
 * @author Andrey Popov
 *
 */
public enum ColumnOrder {
	FIRST           (1),
	SECOND          (2),
	THIRD           (3),
	FOURTH          (4),
	FIFTH           (5),
	FIFTH_FROM_END  (-5 + Integer.MAX_VALUE),
	FOURTH_FROM_END (-4 + Integer.MAX_VALUE),
	THIRD_FROM_END  (-3 + Integer.MAX_VALUE),
	ONE_BUT_LAST    (-2 + Integer.MAX_VALUE),
	LAST            (-1 + Integer.MAX_VALUE);

	private final int levelValue;

	ColumnOrder(int levelValue) {
		this.levelValue = levelValue;
	}

	public int getLevelValue() {
		return levelValue;
	}
}
