package com.mmxlabs.lingo.reports.emissions.columns;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Used as a provider of a priority queue for ordering
 * the fields of emission models to be used in the reflection.
 * @author Andrey Popov
 *
 */
public class ColumnOrderComparator implements Comparator<Field> {
	
	private ColumnOrderComparator() {
		// Hiding public constructor
	}
	
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
	
	/**
	 * Creates new priority queue for ordering the columns by its column level value
	 * @return the priority queue itself
	 */
	public static PriorityQueue<Field> priorityQueue() {
		return new PriorityQueue<>(new ColumnOrderComparator());
	}
}
