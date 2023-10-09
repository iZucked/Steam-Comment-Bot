/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.swt.SWT;

import com.mmxlabs.common.time.DMYUtil;
import com.mmxlabs.common.time.DMYUtil.DayMonthOrder;
import com.mmxlabs.common.util.TriFunction;
import com.mmxlabs.lingo.reports.views.formatters.ScheduleChartFormatters;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.util.PortModelLabeller;
import com.mmxlabs.models.lng.port.util.PortUtil;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.NonShippedIdle;
import com.mmxlabs.models.lng.schedule.NonShippedJourney;
import com.mmxlabs.models.lng.schedule.NonShippedSlotVisit;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.widgets.schedulechart.IScheduleEventLabelElementGenerator;
import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements.Padding;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEventLabel;
import com.mmxlabs.widgets.schedulechart.draw.RelativeBoundsCalculationFunctions;
import com.mmxlabs.widgets.schedulechart.draw.RelativeDrawableElement.RelativeBounds;

public class NinetyDayLabelFactory {
	
	private NinetyDayLabelFactory() {}
	
	public static List<List<IScheduleEventLabelElementGenerator>> buildShowDaysLabels() {
		final List<List<IScheduleEventLabelElementGenerator>> alignments = new ArrayList<>();
		alignments.add(List.of(getLabelElementGeneratorFromTextGenerator(RelativeBoundsCalculationFunctions.CENTER_100P, SWT.CENTER, new Padding(3, 3, 0, 0), se -> {
			return ScheduleChartFormatters.formatAsDays(se.getStart().until(se.getEnd(), ChronoUnit.DAYS));
		})));
		alignments.add(List.of(getLabelElementGeneratorFromTextGenerator(RelativeBoundsCalculationFunctions.CENTER_100P, SWT.CENTER, new Padding(3, 3, 0, 0), se -> {
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
		final int horizontalPadding = 2;
		final Padding labelPadding = new Padding(horizontalPadding, horizontalPadding, 0, 0);
		alignments.add(List.of(
				getLabelElementGeneratorFromTextGenerator(RelativeBoundsCalculationFunctions.LEFT_100P, SWT.LEFT, labelPadding,
						se -> getDestinationLabel(se, Event::getPreviousEvent, Journey::getPort, NonShippedJourney::getPort, NinetyDayLabelFactory::formatDateMedium)),
				getLabelElementGeneratorFromTextGenerator(RelativeBoundsCalculationFunctions.RIGHT_100P, SWT.RIGHT, labelPadding,
						se -> getDestinationLabel(se, Event::getNextEvent, Journey::getDestination, NonShippedJourney::getDestination, NinetyDayLabelFactory::formatDateMedium))));

		alignments.add(List.of(
				getLabelElementGeneratorFromTextGenerator(RelativeBoundsCalculationFunctions.LEFT_100P, SWT.LEFT, labelPadding,
						se -> getDestinationLabel(se, Event::getPreviousEvent, Journey::getPort, NonShippedJourney::getPort, NinetyDayLabelFactory::formatDateShort)),
				getLabelElementGeneratorFromTextGenerator(RelativeBoundsCalculationFunctions.RIGHT_100P, SWT.RIGHT, labelPadding,
						se -> getDestinationLabel(se, Event::getNextEvent, Journey::getDestination, NonShippedJourney::getDestination, NinetyDayLabelFactory::formatDateShort))));

		alignments.add(List.of(
				getLabelElementGeneratorFromTextGenerator(RelativeBoundsCalculationFunctions.LEFT_100P, SWT.LEFT, labelPadding,
						se -> getDestinationLabel(se, Event::getPreviousEvent, Journey::getPort, NonShippedJourney::getPort, zdt -> "")),
				getLabelElementGeneratorFromTextGenerator(RelativeBoundsCalculationFunctions.RIGHT_100P, SWT.RIGHT, labelPadding,
						se -> getDestinationLabel(se, Event::getNextEvent, Journey::getDestination, NonShippedJourney::getDestination, zdt -> ""))));

		return alignments;
	}
	
	public static List<List<IScheduleEventLabelElementGenerator>> buildCanalLabels() {
		final List<List<IScheduleEventLabelElementGenerator>> alignments = new ArrayList<>();
		alignments.add(List.of(getLabelElementGeneratorFromTextGenerator(RelativeBoundsCalculationFunctions.CENTER_100P, SWT.CENTER, new Padding(3, 3, 0, 0), se -> {
			final Object object = se.getData();
			if (object instanceof Journey j) {
				final RouteOption routeOption = j.getRouteOption();
				if (routeOption != RouteOption.DIRECT) {
					return PortModelLabeller.getShortName(routeOption);
				}
			}
			return "";
		})));
		return alignments;
	}
	
	private static IScheduleEventLabelElementGenerator getLabelElementGeneratorFromTextGenerator(RelativeBounds relBounds, int textAlignment, Padding p, Function<ScheduleEvent, String> textGenerator) {
		return (se, dse) -> {
			final var elem = new DrawableScheduleEventLabel(dse.getLabelTextColour(), textGenerator, textAlignment, p, se);
			elem.setRelativeBounds(relBounds);
			return elem;
		};
	}
	
	private static String getDestinationLabel(final ScheduleEvent se, Function<Event, Event> advancer, Function<Journey, Port> portProvider, Function<NonShippedJourney, Port> nonShippedPortProvider, Function<ZonedDateTime, String> dateFormatter) {
		
		Function<Object, String> getNameForBallast = o -> {
			Port p = (o instanceof Journey j) ? portProvider.apply(j) : (o instanceof NonShippedJourney nsj) ? nonShippedPortProvider.apply(nsj) : null;
			if (p != null) {
				final String name = PortUtil.getShortOrFullName(p);
				if (name != null) {
					return name;
				}
			}
			return "";
		};
		
		TriFunction<String, ZonedDateTime, Supplier<Port>, String> getNameForLaden = (cp, date, portSupplier) -> {
				final StringBuilder sb = new StringBuilder();
				final String firstPart = getCpOrPort(cp, portSupplier);
				final String secondPart = dateFormatter.apply(date);
				if (!firstPart.isBlank()) {
					sb.append(firstPart);
					if (!secondPart.isBlank()) {
						sb.append(" ");
						sb.append(secondPart);
					}
				}
				return sb.toString();
		};
		
		final Object object = se.getData();
		if (object instanceof Journey j) {
			if (!j.isLaden()) {
				return getNameForBallast.apply(j);
			} else {
				Event next = advancer.apply(j);
				if (next instanceof Idle) {
					next = advancer.apply(next);
				}
				if (next instanceof final SlotVisit sv) {
					return getNameForLaden.apply(sv.getSlotAllocation().getSlot().getSlotOrDelegateCounterparty(), sv.getStart(), () -> portProvider.apply(j));
				}
			}

		} else if (object instanceof NonShippedJourney j) {
			if (!j.isLaden()) {
				return getNameForBallast.apply(j);
			} else {
				Event next = advancer.apply(j);
				if (next instanceof NonShippedIdle) {
					next = advancer.apply(next);
				}
				if (next instanceof final NonShippedSlotVisit sv) {
					return getNameForLaden.apply(sv.getSlot().getSlotOrDelegateCounterparty(), sv.getStart(), () -> nonShippedPortProvider.apply(j));
				}
			}
		}

		return "";
	}
	
	private static String getCpOrPort(final String counterparty, final Supplier<Port> portProvider) {
		if (counterparty == null || counterparty.isBlank()) {
			final Port port = portProvider.get();
			if (port != null) {
				final String name = PortUtil.getShortOrFullName(port);
				if (name != null) {
					return name;
				}
			}
			return "";
		}
		return counterparty;
	}
	
	private static String formatDateShort(ZonedDateTime zdt) {
		final DayMonthOrder order = DMYUtil.getDayMonthOrder();
		final String pattern = order == DayMonthOrder.DAY_MONTH ? "d/M" : "M/d";
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return "[" + zdt.format(formatter) + "]";
	}

	private static String formatDateMedium(ZonedDateTime zdt) {
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM");
		return "[" + zdt.format(formatter) + "]";
	}
}
