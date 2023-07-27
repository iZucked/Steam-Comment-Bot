package com.mmxlabs.lingo.reports.emissions.columns;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;

import com.mmxlabs.common.Pair;

/**
 * Compares by ColumnOrderLevel
 * 
 * @author Andrey Popov
 */
public class ColumnOrderComparator {
	
	private ColumnOrderComparator() {
	}

	/**
	 * Comparator for fields. With in groups
	 */
	@SuppressWarnings({ "null", "unused" })
	public static final Comparator<Field> byField = (firstField, secondField) -> {
		final ColumnOrderLevel firstFieldColumnLevel = firstField.getAnnotation(ColumnOrderLevel.class);
		final ColumnOrderLevel secondFieldColumnLevel = secondField.getAnnotation(ColumnOrderLevel.class);
		if (firstFieldColumnLevel == null && secondFieldColumnLevel == null) {
			return 0;
		}
		if (firstFieldColumnLevel == null) {
			return -1;
		}
		if (secondFieldColumnLevel == null) {
			return 1;
		}
		final int firstLevel = firstFieldColumnLevel.value().getLevelValue();
		final int secondLevel = secondFieldColumnLevel.value().getLevelValue();
		return Integer.compare(firstLevel, secondLevel);
	};

	/**
	 * Also comparator for column groups
	 */
	public static final Comparator<Pair<ColumnGroup, List<Field>>> byGroup = (firstEntry, secondEntry) -> {
		final int firstLevel = firstEntry.getFirst().position().getLevelValue();
		final int secondLevel = secondEntry.getFirst().position().getLevelValue();
		return Integer.compare(firstLevel, secondLevel);
	};
}
