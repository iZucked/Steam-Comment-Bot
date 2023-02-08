package org.eclipse.nebula.widgets.ganttchart.plaque;

import org.eclipse.nebula.widgets.ganttchart.DateHelper;
import org.eclipse.nebula.widgets.ganttchart.GanttEvent;

public class DaysContentProvider implements IPlaqueContentProvider {

	private final String formatString;
	private int numDecimalPlaces;

	public DaysContentProvider(final int numDecimalPlaces) {
		this.numDecimalPlaces = numDecimalPlaces <= 0 ? 0 : numDecimalPlaces;
		formatString = String.format("%%.%df", numDecimalPlaces);
	}

	@Override
	public String provideContents(GanttEvent event) {
		final long hours = DateHelper.hoursBetween(event.getActualStartDate(), event.getActualEndDate(), false);
		final long integerDivDays = hours / 24L;
		final long remainingHours = hours % 24L;
		if (numDecimalPlaces == 0) {
			final long valueToShow = remainingHours < 12 ? integerDivDays : integerDivDays + 1;
			return Long.toString(valueToShow);
		} else {
			final double valueToShow = integerDivDays + remainingHours / 24.0;
			return String.format(formatString, valueToShow);
		}
	}

	public void setNumDecimalPlaces(final int numDecimalPlaces) {
		this.numDecimalPlaces = numDecimalPlaces;
	}

	public int getNumDecimalPlaces() {
		return numDecimalPlaces;
	}
}
