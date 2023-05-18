package com.mmxlabs.lingo.reports.emissions.columns;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.PriorityQueue;

public class ColumnOrderComparator implements Comparator<Field> {
	
	private ColumnOrderComparator() {}
	
	@SuppressWarnings({ "unused", "null" })
	@Override
	public int compare(final Field firstField, final Field secondField) {
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
		final int firstLevel = firstFieldColumnLevel.level().getLevelValue();
		final int secondLevel = secondFieldColumnLevel.level().getLevelValue();
		return Integer.compare(firstLevel, secondLevel);
	}
	
	public static PriorityQueue<Field> priorityQueue() {
		return new PriorityQueue<Field>(new ColumnOrderComparator());
	}
}
