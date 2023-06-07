package com.mmxlabs.lingo.reports.emissions.columns;

/**
 * Used for structuring the order of columns for the C02 emission reports
 * @author Andrey Popov
 *
 */
public enum ColumnOrder {
	ID_LEVEL(Integer.MIN_VALUE),
	START(Integer.MIN_VALUE + 100),
	EARLY_START_DATE(Integer.MIN_VALUE + 200),
	EARLY_END_DATE(Integer.MIN_VALUE + 200 + 1),
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
