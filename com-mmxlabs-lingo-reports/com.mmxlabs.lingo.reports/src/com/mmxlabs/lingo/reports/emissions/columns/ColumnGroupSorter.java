package com.mmxlabs.lingo.reports.emissions.columns;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.modelbased.annotations.LingoIgnore;

/**
 * Black box which orders column groups and columns within groups
 * @author Andrey Popov
 *
 */
public class ColumnGroupSorter {
	
	public static List<Pair<ColumnGroup, List<Field>>> sortGroups(final Field[] fields) {
		final ColumnGroupSorter sorter = new ColumnGroupSorter();
		for (final Field field : fields) {
			if (field.getAnnotation(LingoIgnore.class) != null) {
				continue;
			}
			final ColumnGroup group = field.getAnnotation(ColumnGroup.class);
			if (group != null) {
				sorter.put(group, field);
			}
		}
		for (final Pair<ColumnGroup, List<Field>> entry :  sorter.entries) {
			entry.getSecond().sort(ColumnOrderComparator.byField);
		}
		sorter.entries.sort(ColumnOrderComparator.byGroup);
		return sorter.entries;
	}
	
	private final List<Pair<ColumnGroup, List<Field>>> entries;

	private ColumnGroupSorter() {
		entries = new LinkedList<>();
	}
	
	private void put(final ColumnGroup group, final Field field) {
		for (final Pair<ColumnGroup, List<Field>> entry : entries) {
			if (entry.getFirst().id().equals(group.id())) {
				entry.getSecond().add(field);
				return;
			}
		}
		List<Field> entryFields = new LinkedList<>();
		entryFields.add(field);
		entries.add(new Pair<>(group, entryFields));
	}
}
