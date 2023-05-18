package com.mmxlabs.lingo.reports.emissions.columns;

/**
 * Used for structuring the order of columns for the C02 emission reports
 * @author Andrey Popov
 *
 */
public enum ColumnOrder {
	START(Integer.MIN_VALUE),
	EARLY_LEVEL(0),
	MIDDLE_LEVEL(100),
	LATER_LEVEL(200),
	END(Integer.MAX_VALUE);

	private final int levelValue;

	ColumnOrder(int levelValue) {
		this.levelValue = levelValue;
	}

	public int getLevelValue() {
		return levelValue;
	}
}
