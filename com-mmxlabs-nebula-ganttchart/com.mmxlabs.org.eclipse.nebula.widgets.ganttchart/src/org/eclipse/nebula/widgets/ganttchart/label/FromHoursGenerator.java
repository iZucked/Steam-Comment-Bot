package org.eclipse.nebula.widgets.ganttchart.label;

import java.util.function.LongFunction;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.nebula.widgets.ganttchart.DateHelper;
import org.eclipse.nebula.widgets.ganttchart.GanttEvent;

public class FromHoursGenerator implements IFromEventTextGenerator {

	private final @NonNull LongFunction<String> formatter;

	public FromHoursGenerator(final @NonNull LongFunction<String> formatter) {
		this.formatter = formatter;
	}

	private long calculateHours(GanttEvent event) {
		return DateHelper.hoursBetween(event.getActualStartDate(), event.getActualEndDate(), false);
	}

	@Override
	public String generateText(GanttEvent event) {
		final long hours = calculateHours(event);
		return formatter.apply(hours);
	}

}