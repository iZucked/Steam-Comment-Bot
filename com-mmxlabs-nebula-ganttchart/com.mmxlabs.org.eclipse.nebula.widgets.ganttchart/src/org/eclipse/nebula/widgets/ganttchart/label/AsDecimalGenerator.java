package org.eclipse.nebula.widgets.ganttchart.label;

import java.util.function.LongFunction;

import org.eclipse.jdt.annotation.NonNull;

public class AsDecimalGenerator extends FromHoursGenerator {

	public AsDecimalGenerator(final int numDecimalPlaces) {
		super(generateFormatter(numDecimalPlaces));
	}
	
	private static @NonNull LongFunction<String> generateFormatter(int numDecimalPlaces) {
		if (numDecimalPlaces < 0) {
			numDecimalPlaces = 0;
		}
		final int numDp = numDecimalPlaces;
		final String formatString = String.format("%%.%df", numDecimalPlaces);
		return hours -> {
			final long integerDivDays = hours / 24L;
			final long remainingHours = hours % 24L;
			if (numDp == 0) {
				final long valueToShow = remainingHours < 12 ? integerDivDays : integerDivDays + 1;
				return Long.toString(valueToShow);
			} else {
				final double valueToShow = integerDivDays + remainingHours / 24.0;
				return String.format(formatString, valueToShow);
			}
		};
	}
}
