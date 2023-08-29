package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.eclipse.swt.SWT;

import com.mmxlabs.lingo.reports.views.formatters.ScheduleChartFormatters;
import com.mmxlabs.widgets.schedulechart.IScheduleEventLabelElementGenerator;
import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEventLabel;
import com.mmxlabs.widgets.schedulechart.draw.RelativeBoundsCalculationFunctions;
import com.mmxlabs.widgets.schedulechart.draw.RelativeDrawableElement.RelativeBounds;

public class NinetyDayLabelFactory {
	
	public static List<List<IScheduleEventLabelElementGenerator>> buildShowDaysLabels() {
		final List<List<IScheduleEventLabelElementGenerator>> alignments = new ArrayList<>();
		alignments.add(List.of(getLabelElementGeneratorFromTextGenerator(RelativeBoundsCalculationFunctions.CENTER_100P, SWT.CENTER, se -> {
			return ScheduleChartFormatters.formatAsDays(se.getStart().until(se.getEnd(), ChronoUnit.DAYS));
		})));
		alignments.add(List.of(getLabelElementGeneratorFromTextGenerator(RelativeBoundsCalculationFunctions.CENTER_100P, SWT.CENTER, se -> {
			long hours = se.getStart().until(se.getEnd(), ChronoUnit.DAYS);
			final long integerDivDays = hours / 24L;
			final long remainingHours = hours % 24L;
			final long valueToShow = remainingHours < 12 ? integerDivDays : integerDivDays + 1;
			return Long.toString(valueToShow);
		})));
		return alignments;
	}
	
	public static List<List<IScheduleEventLabelElementGenerator>> buildDestinationLabels() {
		final List<List<IScheduleEventLabelElementGenerator>> alignments = new ArrayList<>();
		return alignments;
	}
	
	public static List<List<IScheduleEventLabelElementGenerator>> buildCanalLabels() {
		final List<List<IScheduleEventLabelElementGenerator>> alignments = new ArrayList<>();
		return alignments;
	}
	
	private static IScheduleEventLabelElementGenerator getLabelElementGeneratorFromTextGenerator(RelativeBounds relBounds, int textAlignment, Function<ScheduleEvent, String> textGenerator) {
		return (se, labelTextColour, labelBgColour) -> {
			final var elem = new DrawableScheduleEventLabel(labelTextColour, labelBgColour, textGenerator, textAlignment, se);
			elem.setRelativeBounds(relBounds);
			return elem;
		};
	}

}
