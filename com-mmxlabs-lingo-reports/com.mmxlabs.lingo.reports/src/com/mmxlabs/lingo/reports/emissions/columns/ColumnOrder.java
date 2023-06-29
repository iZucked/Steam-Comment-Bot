package com.mmxlabs.lingo.reports.emissions.columns;

/**
 * Used for structuring the order of columns for the C02 emission reports
 * @author Andrey Popov
 *
 */
public enum ColumnOrder {
	ID_LEVEL(Integer.MIN_VALUE),
	START(Integer.MIN_VALUE + 100),
	EARLY_LEVEL(0),
	START_DATE(100),
	END_DATE(101),
	MIDDLE_LEVEL(200),
	LATER_LEVEL(300),
	END(Integer.MAX_VALUE);

	private final int levelValue;

	ColumnOrder(int levelValue) {
		this.levelValue = levelValue;
	}

	public int getLevelValue() {
		return levelValue;
	}
}
