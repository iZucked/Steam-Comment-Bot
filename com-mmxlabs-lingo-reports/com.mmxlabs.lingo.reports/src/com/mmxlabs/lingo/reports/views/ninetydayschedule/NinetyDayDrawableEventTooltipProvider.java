/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.JourneyEventTooltip;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.PortEventTooltip;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.buysell.BuySellEventTooltip;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.buysell.PositionsSequenceElement;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.ui.date.DateTimeFormatsProvider;
import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.ScheduleEventTooltip;
import com.mmxlabs.widgets.schedulechart.ScheduleEventTooltip.ScheduleEventTooltipBuilder;
import com.mmxlabs.widgets.schedulechart.ScheduleEventTooltip.ScheduleEventTooltipBuilder.ScheduleEventTooltipData;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEventTooltip;
import com.mmxlabs.widgets.schedulechart.providers.IDrawableScheduleEventTooltipProvider;

public class NinetyDayDrawableEventTooltipProvider implements IDrawableScheduleEventTooltipProvider {

	/**
	 * Note that PositionsSequenceToolTip only has title and relies on other stuff when determining its body content
	 */
	@Override
	public Optional<ScheduleEventTooltip> getTooltip(final ScheduleEvent scheduleEvent) {
		final Object scheduleEventData = scheduleEvent.getData();
		final ScheduleEventTooltipBuilder tooltipBuilder = ScheduleEventTooltip.of(scheduleEvent);

		if (scheduleEventData instanceof final Event event) {
			final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateTimeFormatsProvider.INSTANCE.getYearMonthStringDisplay());
			if (event instanceof Journey) {
				tooltipBuilder.add(ScheduleEventTooltipData.HEADER_NAME, Journey.class, j -> j.getPort().getName()) //
						.add(ScheduleEventTooltipData.HEADER_NAME, Journey.class, j -> j.getDestination().getName()) //
						.add(ScheduleEventTooltipData.EVENT_TYPE, Journey.class, j -> j.isLaden() ? "Laden" : "Ballast") //
						.addBodyField("ID", Journey.class, Event::name) //
						.addBodyField("Start", Journey.class, j -> j.getStart().format(dateTimeFormatter)) //
						.addBodyField("End", Journey.class, j -> j.getEnd().format(dateTimeFormatter)); //
			} else if (event instanceof Idle) {
				tooltipBuilder.add(ScheduleEventTooltipData.HEADER_NAME, Idle.class, i -> (i.getPort() == null) ? "" : i.getPort().getName());
			} else if (event instanceof PortVisit) {
				tooltipBuilder.add(ScheduleEventTooltipData.HEADER_NAME, PortVisit.class, pv -> pv.getPort().getName());
			}
		} else if (scheduleEventData instanceof PositionsSequenceElement) {
			tooltipBuilder.add(ScheduleEventTooltipData.HEADER_NAME, dontCareLambda -> null);
		}

		if (tooltipBuilder.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(tooltipBuilder.create());
	}

	@SuppressWarnings("null") // Eclipse does not like Optionals. Neither do I like warnings.
	@Override
	public Optional<DrawableScheduleEventTooltip> getDrawableTooltip(ScheduleEventTooltip tooltip) {
		final Object scheduleEventData = tooltip.se().getData();
		if (scheduleEventData instanceof final Event event) {
			if (event instanceof Journey) {
				return Optional.of(new JourneyEventTooltip(tooltip));
			} else if (event instanceof Idle || event instanceof PortVisit) {
				return Optional.of(new PortEventTooltip(tooltip));
			}
		}
		if (scheduleEventData instanceof final PositionsSequenceElement positionsSequenceElement) {
			return Optional.of(new BuySellEventTooltip(tooltip, positionsSequenceElement));
		}
		return Optional.empty();
	}

}
