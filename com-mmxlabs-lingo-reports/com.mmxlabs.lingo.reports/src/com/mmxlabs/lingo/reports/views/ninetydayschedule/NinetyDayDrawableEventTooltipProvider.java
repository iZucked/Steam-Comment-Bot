/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.JourneyEventTooltip;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.PortEventTooltip;
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

	@Override
	public Optional<ScheduleEventTooltip> getTooltip(ScheduleEvent event) {
		if (!(event.getData() instanceof Event)) return Optional.empty();
		Event e = (Event) event.getData();

		ScheduleEventTooltipBuilder tooltip = ScheduleEventTooltip.of(event);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DateTimeFormatsProvider.INSTANCE.getYearMonthStringDisplay());
		if (e instanceof Journey) {
			tooltip
				.add(ScheduleEventTooltipData.HEADER_NAME, Journey.class, j -> j.getPort().getName())
				.add(ScheduleEventTooltipData.HEADER_NAME, Journey.class, j -> j.getDestination().getName())
				.add(ScheduleEventTooltipData.EVENT_TYPE, Journey.class, j -> j.isLaden() ? "Laden" : "Ballast")
				.addBodyField("ID", Journey.class, Event::name)
				.addBodyField("Start", Journey.class, j -> j.getStart().format(dtf))
				.addBodyField("End", Journey.class, j -> j.getEnd().format(dtf));
				
		} else if (e instanceof Idle) {
			tooltip
				.add(ScheduleEventTooltipData.HEADER_NAME, Idle.class, i -> (i.getPort() == null) ? "" : i.getPort().getName());
		} else if (e instanceof PortVisit) {
			tooltip
				.add(ScheduleEventTooltipData.HEADER_NAME, PortVisit.class, pv -> pv.getPort().getName());
		}

		return tooltip.isEmpty() ? Optional.empty() : Optional.of(tooltip.create());
	}

	@Override
	public Optional<DrawableScheduleEventTooltip> getDrawableTooltip(ScheduleEventTooltip tooltip) {
		if (!(tooltip.se().getData() instanceof Event)) return Optional.empty();
		Event e = (Event) tooltip.se().getData();

		if (e instanceof Journey) {
			return Optional.of(new JourneyEventTooltip(tooltip));
		} else if (e instanceof Idle || e instanceof PortVisit) {
			return Optional.of(new PortEventTooltip(tooltip));
		}

		return Optional.empty();
	}

}
