package com.mmxlabs.widgets.schedulechart.providers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.mmxlabs.widgets.schedulechart.ScheduleChartRow;
import com.mmxlabs.widgets.schedulechart.ScheduleEvent;

public interface IScheduleEventProvider<T> {
	List<ScheduleEvent> getEvents(T input);

	String getKeyForEvent(ScheduleEvent event);

	default List<ScheduleChartRow> classifyEventsIntoRows(List<ScheduleEvent> events) {
		return events.stream().collect(Collectors.toMap(this::getKeyForEvent, e -> new ArrayList<>(List.of(e)), (existing, replacement) -> {
			existing.addAll(replacement);
			return existing;
		})).entrySet().stream().map(e -> new ScheduleChartRow(e.getKey(), e.getValue())).toList();
	}

}
