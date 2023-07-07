/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.services;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.nebula.widgets.ganttchart.GanttEvent;
import org.eclipse.nebula.widgets.ganttchart.label.AsDecimalGenerator;
import org.eclipse.nebula.widgets.ganttchart.label.EEventLabelAlignment;
import org.eclipse.nebula.widgets.ganttchart.label.FromHoursGenerator;
import org.eclipse.nebula.widgets.ganttchart.label.IEventTextPropertiesGenerator;
import org.eclipse.nebula.widgets.ganttchart.label.IFromEventTextGenerator;

import com.mmxlabs.common.time.DMYUtil;
import com.mmxlabs.common.time.DMYUtil.DayMonthOrder;
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

@NonNullByDefault
public interface IScheduleLabelAlignmentMapFactory {

	default List<Map<EEventLabelAlignment, IEventTextPropertiesGenerator>> buildShowDaysLabelAlignmentMaps() {
		final List<List<IEventTextPropertiesGenerator>> alignments = new ArrayList<>();
		final List<IFromEventTextGenerator> textGenerators = new ArrayList<>(2);
		textGenerators.add(new FromHoursGenerator(ScheduleChartFormatters::formatAsDays));
		textGenerators.add(new AsDecimalGenerator(0));
		for (final IFromEventTextGenerator textGenerator : textGenerators) {
			final List<IEventTextPropertiesGenerator> alignment = new ArrayList<>();
			alignment.add(new IEventTextPropertiesGenerator() {

				@Override
				public EEventLabelAlignment getAlignment() {
					return EEventLabelAlignment.CENTRE;
				}

				@Override
				public String generateText(final GanttEvent event) {
					return textGenerator.generateText(event);
				}
			});
			alignments.add(alignment);
		}
		return alignments.stream() //
				.map(l -> l.stream().collect(Collectors.toMap(IEventTextPropertiesGenerator::getAlignment, Function.identity()))) //
				.toList();
	}

	default List<Map<EEventLabelAlignment, IEventTextPropertiesGenerator>> buildDestinationLabelAlignmentMaps() {
		final List<List<IEventTextPropertiesGenerator>> alignments = new ArrayList<>();
		{
			final List<IEventTextPropertiesGenerator> alignment = new ArrayList<>();
			alignment.add(new IEventTextPropertiesGenerator() {

				@Override
				public EEventLabelAlignment getAlignment() {
					return EEventLabelAlignment.LEFT;
				}

				@Override
				public String generateText(final GanttEvent event) {
					final Object object = event.getData();
					if (object instanceof Journey j) {
						if (!j.isLaden()) {
							final Port port = j.getPort();
							if (port != null) {
								final String name = PortUtil.getShortOrFullName(port);
								if (name != null) {
									return name;
								}
							}
						} else {
							if (j.getPreviousEvent() instanceof final SlotVisit sv) {
								final StringBuilder sb = new StringBuilder();
								final String firstPart = getCpOrPort(sv.getSlotAllocation().getSlot().getSlotOrDelegateCounterparty(), j::getPort);
								if (!firstPart.isBlank()) {
									sb.append(firstPart);
									sb.append(" ");
								}
								final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM");
								sb.append("[");
								sb.append(sv.getStart().format(formatter));
								sb.append("]");
								return sb.toString();
							}
						}
					} else if (object instanceof NonShippedJourney j) {
						if (!j.isLaden()) {
							final Port port = j.getPort();
							if (port != null) {
								final String name = PortUtil.getShortOrFullName(port);
								if (name != null) {
									return name;
								}
							}
						} else {
							if (j.getPreviousEvent() instanceof final NonShippedSlotVisit sv) {
								final @NonNull StringBuilder sb = new StringBuilder();
								final @NonNull String firstPart = getCpOrPort(sv.getSlot().getSlotOrDelegateCounterparty(), j::getPort);
								if (!firstPart.isBlank()) {
									sb.append(firstPart);
									sb.append(" ");
								}
								final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM");
								sb.append("[");
								sb.append(sv.getStart().format(formatter));
								sb.append("]");
								return sb.toString();
							}
						}
					}
					return "";
				}
			});
			alignment.add(new IEventTextPropertiesGenerator() {

				@Override
				public EEventLabelAlignment getAlignment() {
					return EEventLabelAlignment.RIGHT;
				}

				@Override
				public String generateText(final GanttEvent event) {
					final Object object = event.getData();
					if (object instanceof Journey j) {
						if (!j.isLaden()) {
							final Port port = j.getDestination();
							if (port != null) {
								final String name = PortUtil.getShortOrFullName(port);
								if (name != null) {
									return name;
								}
							}
						} else {
							Event next = j.getNextEvent();
							if (next instanceof Idle) {
								next = next.getNextEvent();
							}
							if (next instanceof final SlotVisit sv) {
								final StringBuilder sb = new StringBuilder();
								final String firstPart = getCpOrPort(sv.getSlotAllocation().getSlot().getSlotOrDelegateCounterparty(), j::getDestination);
								if (!firstPart.isBlank()) {
									sb.append(firstPart);
									sb.append(" ");
								}
								final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM");
								sb.append("[");
								sb.append(sv.getStart().format(formatter));
								sb.append("]");
								return sb.toString();
							}
						}
					} else if (object instanceof NonShippedJourney j) {
						if (!j.isLaden()) {
							final Port port = j.getDestination();
							if (port != null) {
								final String name = PortUtil.getShortOrFullName(port);
								if (name != null) {
									return name;
								}
							}
						} else {
							Event next = j.getNextEvent();
							if (next instanceof NonShippedIdle) {
								next = next.getNextEvent();
							}
							if (next instanceof final NonShippedSlotVisit sv) {
								final @NonNull StringBuilder sb = new StringBuilder();
								final @NonNull String firstPart = getCpOrPort(sv.getSlot().getSlotOrDelegateCounterparty(), j::getDestination);
								if (!firstPart.isBlank()) {
									sb.append(firstPart);
									sb.append(" ");
								}
								final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM");
								sb.append("[");
								sb.append(sv.getStart().format(formatter));
								sb.append("]");
								return sb.toString();
							}
						}
					}
					return "";
				}
			});
			alignments.add(alignment);
		}
		{
			final List<IEventTextPropertiesGenerator> alignment = new ArrayList<>();
			alignment.add(new IEventTextPropertiesGenerator() {

				@Override
				public EEventLabelAlignment getAlignment() {
					return EEventLabelAlignment.LEFT;
				}

				@Override
				public String generateText(final GanttEvent event) {
					final Object object = event.getData();
					if (object instanceof Journey j) {
						if (!j.isLaden()) {
							final Port port = j.getPort();
							if (port != null) {
								final String name = PortUtil.getShortOrFullName(port);
								if (name != null) {
									return name;
								}
							}
						} else {
							if (j.getPreviousEvent() instanceof final SlotVisit sv) {
								final StringBuilder sb = new StringBuilder();
								final String firstPart = getCpOrPort(sv.getSlotAllocation().getSlot().getSlotOrDelegateCounterparty(), j::getPort);
								if (!firstPart.isBlank()) {
									sb.append(firstPart);
									sb.append(" ");
								}
								final DayMonthOrder order = DMYUtil.getDayMonthOrder();
								final String pattern = order == DayMonthOrder.DAY_MONTH ? "d/M" : "M/d";
								final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
								sb.append("[");
								sb.append(sv.getStart().format(formatter));
								sb.append("]");
								return sb.toString();
							}
						}
					} else if (object instanceof NonShippedJourney j) {
						if (!j.isLaden()) {
							final Port port = j.getPort();
							if (port != null) {
								final String name = PortUtil.getShortOrFullName(port);
								if (name != null) {
									return name;
								}
							}
						} else {
							if (j.getPreviousEvent() instanceof final NonShippedSlotVisit sv) {
								final @NonNull StringBuilder sb = new StringBuilder();
								final @NonNull String firstPart = getCpOrPort(sv.getSlot().getSlotOrDelegateCounterparty(), j::getPort);
								if (!firstPart.isBlank()) {
									sb.append(firstPart);
									sb.append(" ");
								}
								final DayMonthOrder order = DMYUtil.getDayMonthOrder();
								final String pattern = order == DayMonthOrder.DAY_MONTH ? "d/M" : "M/d";
								final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
								sb.append("[");
								sb.append(sv.getStart().format(formatter));
								sb.append("]");
								return sb.toString();
							}
						}

					}
					return "";
				}
			});
			alignment.add(new IEventTextPropertiesGenerator() {

				@Override
				public EEventLabelAlignment getAlignment() {
					return EEventLabelAlignment.RIGHT;
				}

				@Override
				public String generateText(final GanttEvent event) {
					final Object object = event.getData();
					if (object instanceof Journey j) {
						if (!j.isLaden()) {
							final Port port = j.getDestination();
							if (port != null) {
								final String name = PortUtil.getShortOrFullName(port);
								if (name != null) {
									return name;
								}
							}
						} else {
							Event next = j.getNextEvent();
							if (next instanceof Idle) {
								next = next.getNextEvent();
							}
							if (next instanceof final SlotVisit sv) {
								final StringBuilder sb = new StringBuilder();
								final String firstPart = getCpOrPort(sv.getSlotAllocation().getSlot().getSlotOrDelegateCounterparty(), j::getDestination);
								if (!firstPart.isBlank()) {
									sb.append(firstPart);
									sb.append(" ");
								}
								final DayMonthOrder order = DMYUtil.getDayMonthOrder();
								final String pattern = order == DayMonthOrder.DAY_MONTH ? "d/M" : "M/d";
								final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
								sb.append("[");
								sb.append(sv.getStart().format(formatter));
								sb.append("]");
								return sb.toString();
							}
						}
					} else if (object instanceof NonShippedJourney j) {
						if (!j.isLaden()) {
							final Port port = j.getDestination();
							if (port != null) {
								final String name = PortUtil.getShortOrFullName(port);
								if (name != null) {
									return name;
								}
							}
						} else {
							Event next = j.getNextEvent();
							if (next instanceof NonShippedIdle) {
								next = next.getNextEvent();
							}
							if (next instanceof final NonShippedSlotVisit sv) {
								final @NonNull StringBuilder sb = new StringBuilder();
								final @NonNull String firstPart = getCpOrPort(sv.getSlot().getSlotOrDelegateCounterparty(), j::getDestination);
								if (!firstPart.isBlank()) {
									sb.append(firstPart);
									sb.append(" ");
								}
								final DayMonthOrder order = DMYUtil.getDayMonthOrder();
								final String pattern = order == DayMonthOrder.DAY_MONTH ? "d/M" : "M/d";
								final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
								sb.append("[");
								sb.append(sv.getStart().format(formatter));
								sb.append("]");
								return sb.toString();
							}
						}

					}
					return "";
				}
			});
			alignments.add(alignment);
		}
		{
			final List<IEventTextPropertiesGenerator> alignment = new ArrayList<>();
			alignment.add(new IEventTextPropertiesGenerator() {

				@Override
				public EEventLabelAlignment getAlignment() {
					return EEventLabelAlignment.LEFT;
				}

				@Override
				public String generateText(final GanttEvent event) {
					final Object object = event.getData();
					if (object instanceof Journey j) {
						if (!j.isLaden()) {
							final Port port = j.getPort();
							if (port != null) {
								final String name = PortUtil.getShortOrFullName(port);
								if (name != null) {
									return name;
								}
							}
						} else {
							if (j.getPreviousEvent() instanceof final SlotVisit sv) {
								final String firstPart = getCpOrPort(sv.getSlotAllocation().getSlot().getSlotOrDelegateCounterparty(), j::getPort);
								if (!firstPart.isBlank()) {
									return firstPart;
								}
							}
						}
					} else if (object instanceof NonShippedJourney j) {
						if (!j.isLaden()) {
							final Port port = j.getPort();
							if (port != null) {
								final String name = PortUtil.getShortOrFullName(port);
								if (name != null) {
									return name;
								}
							}
						} else {
							if (j.getPreviousEvent() instanceof final NonShippedSlotVisit sv) {
								final @NonNull String firstPart = getCpOrPort(sv.getSlot().getSlotOrDelegateCounterparty(), j::getPort);
								if (!firstPart.isBlank()) {
									return firstPart;
								}
							}
						}
					}
					return "";
				}
			});
			alignment.add(new IEventTextPropertiesGenerator() {

				@Override
				public EEventLabelAlignment getAlignment() {
					return EEventLabelAlignment.RIGHT;
				}

				@Override
				public String generateText(final GanttEvent event) {
					final Object object = event.getData();
					if (object instanceof Journey j) {
						if (!j.isLaden()) {
							final Port port = j.getDestination();
							if (port != null) {
								final String name = PortUtil.getShortOrFullName(port);
								if (name != null) {
									return name;
								}
							}
						} else {
							Event next = j.getNextEvent();
							if (next instanceof Idle) {
								next = next.getNextEvent();
							}
							if (next instanceof final SlotVisit sv) {
								final String firstPart = getCpOrPort(sv.getSlotAllocation().getSlot().getSlotOrDelegateCounterparty(), j::getDestination);
								if (!firstPart.isBlank()) {
									return firstPart;
								}
							}
						}
					} else if (object instanceof NonShippedJourney j) {
						if (!j.isLaden()) {
							final Port port = j.getDestination();
							if (port != null) {
								final String name = PortUtil.getShortOrFullName(port);
								if (name != null) {
									return name;
								}
							}
						} else {
							Event next = j.getNextEvent();
							if (next instanceof NonShippedIdle) {
								next = next.getNextEvent();
							}
							if (next instanceof final NonShippedSlotVisit sv) {
								final @NonNull String firstPart = getCpOrPort(sv.getSlot().getSlotOrDelegateCounterparty(), j::getDestination);
								if (!firstPart.isBlank()) {
									return firstPart;
								}
							}
						}
					}
					return "";
				}
			});
			alignments.add(alignment);
		}
		return alignments.stream() //
				.map(l -> l.stream().collect(Collectors.toMap(IEventTextPropertiesGenerator::getAlignment, Function.identity()))) //
				.collect(Collectors.toList());
	}

	default List<Map<EEventLabelAlignment, IEventTextPropertiesGenerator>> buildCanalAlignmentMaps() {
		final List<List<IEventTextPropertiesGenerator>> alignments = new ArrayList<>();
		{
			final List<IEventTextPropertiesGenerator> alignment = new ArrayList<>();
			alignment.add(new IEventTextPropertiesGenerator() {

				@Override
				public EEventLabelAlignment getAlignment() {
					return EEventLabelAlignment.CENTRE;
				}

				@Override
				public String generateText(final GanttEvent event) {
					final Object object = event.getData();
					if (object instanceof Journey j) {
						final RouteOption routeOption = j.getRouteOption();
						if (routeOption != RouteOption.DIRECT) {
							return PortModelLabeller.getShortName(routeOption);
						}
					}
					return "";
				}
			});
			alignments.add(alignment);
		}
		return alignments.stream() //
				.map(l -> l.stream().collect(Collectors.toMap(IEventTextPropertiesGenerator::getAlignment, Function.identity()))) //
				.toList();
	}

	public static String getCpOrPort(final String counterparty, final Supplier<Port> portProvider) {
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

}
