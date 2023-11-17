/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mmxlabs.lingo.reports.views.formatters.Formatters;
import com.mmxlabs.lingo.reports.views.formatters.Formatters.DurationMode;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.JourneyEventTooltip;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.PortEventTooltip;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.buysell.BuySellEventTooltip;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.buysell.PositionStateType;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.buysell.PositionsSeqenceElements;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.buysell.PositionsSequenceClassification;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.buysell.PositionsSequenceElement;
import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.port.CanalEntry;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.util.PortModelLabeller;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.NonShippedIdle;
import com.mmxlabs.models.lng.schedule.NonShippedJourney;
import com.mmxlabs.models.lng.schedule.NonShippedSlotVisit;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.util.LatenessUtils;
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
		Object scheduleEventData = scheduleEvent.getData();
		ScheduleEventTooltipBuilder tooltipBuilder = ScheduleEventTooltip.of(scheduleEvent);
		
		if (scheduleEventData instanceof PositionsSequenceElement posElement) {
			tooltipBuilder.add(ScheduleEventTooltipData.HEADER_NAME, dontCareLambda -> null);
			scheduleEventData = posElement.getElement();
			tooltipBuilder = ScheduleEventTooltip.of(scheduleEvent, se -> ((PositionsSequenceElement) se.getData()).getElement());
		}
		
		if (scheduleEventData instanceof final Event event) {
			final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateTimeFormatsProvider.INSTANCE.getDateTimeStringDisplay());
			
			tooltipBuilder.addBodyField("ID", Event.class, Event::name) //
			.addBodyField("Start", Event.class, j -> j.getStart().format(dateTimeFormatter)) //
			.addBodyField("End", Event.class, j -> j.getEnd().format(dateTimeFormatter)); //
			
			if (event instanceof Journey) {
				tooltipBuilder.add(ScheduleEventTooltipData.HEADER_NAME, Journey.class, j -> j.getPort().getName()) //
						.add(ScheduleEventTooltipData.HEADER_NAME, Journey.class, j -> j.getDestination().getName()) //
						.add(ScheduleEventTooltipData.EVENT_TYPE, Journey.class, j -> j.isLaden() ? "Laden" : "Ballast") //
						.addBodyField("Travel time", Journey.class, j -> Formatters.formatAsDays(DurationMode.DAYS_HOURS_HUMAN, j.getDuration()));
			} else if (event instanceof NonShippedJourney) {
				tooltipBuilder.add(ScheduleEventTooltipData.HEADER_NAME, NonShippedJourney.class, j -> j.getPort().getName()) //
				.add(ScheduleEventTooltipData.HEADER_NAME, NonShippedJourney.class, j -> j.getDestination().getName()) //
				.add(ScheduleEventTooltipData.EVENT_TYPE, NonShippedJourney.class, j -> j.isLaden() ? "Laden" : "Ballast") //
				.addBodyField("Travel time", NonShippedJourney.class, j -> Formatters.formatAsDays(DurationMode.DAYS_HOURS_HUMAN, j.getDuration()));
			} else if (event instanceof Idle) {
				tooltipBuilder.add(ScheduleEventTooltipData.HEADER_NAME, Idle.class, this::getIdleHeader)
						.addBodyField("Idle time", Idle.class, i -> Formatters.formatAsDays(DurationMode.DAYS_HOURS_HUMAN, i.getDuration()));
			} else if (event instanceof NonShippedIdle) {
				tooltipBuilder.add(ScheduleEventTooltipData.HEADER_NAME, NonShippedIdle.class, this::getIdleHeader)
				.addBodyField("Idle time", NonShippedIdle.class, i -> Formatters.formatAsDays(DurationMode.DAYS_HOURS_HUMAN, i.getDuration()));
			} else if (event instanceof SlotVisit) {
				tooltipBuilder.add(ScheduleEventTooltipData.HEADER_NAME, SlotVisit.class, sv -> "At " + sv.getPort().getName())
						.addBodyField("Time in port", SlotVisit.class, sv -> Formatters.formatAsDays(DurationMode.DAYS_HOURS_HUMAN, sv.getDuration()))
						.addBodyField("Window end", SlotVisit.class, sv -> DateTimeFormatter.ofPattern(DateTimeFormatsProvider.INSTANCE.getDateTimeStringDisplay()).format(sv.getSlotAllocation().getSlot().getSchedulingTimeWindow().getEnd()));
			}
			else if (event instanceof NonShippedSlotVisit) {
				tooltipBuilder.add(ScheduleEventTooltipData.HEADER_NAME, NonShippedSlotVisit.class, sv -> "At " + sv.getPort().getName())
						.addBodyField("Time in port", NonShippedSlotVisit.class, sv -> Formatters.formatAsDays(DurationMode.DAYS_HOURS_HUMAN, sv.getDuration()));
			}
			 else if (event instanceof PortVisit) {
				 tooltipBuilder.add(ScheduleEventTooltipData.HEADER_NAME, PortVisit.class, pv -> "At " + pv.getPort().getName())
					.addBodyField("Time in port", PortVisit.class, pv -> Formatters.formatAsDays(DurationMode.DAYS_HOURS_HUMAN, pv.getDuration()));
			 }
			
			for(String footerText : getEventFooterText(event)) {
				tooltipBuilder.add(ScheduleEventTooltipData.FOOTER_TEXT, Event.class, e -> footerText);
			}
		}
		// For Buy PositionSequence elements
		else if(scheduleEventData instanceof final OpenSlotAllocation) {
			tooltipBuilder.add(ScheduleEventTooltipData.HEADER_NAME, OpenSlotAllocation.class, sa -> "At " + sa.getSlot().getPort().getName())
						.addBodyField("ID", OpenSlotAllocation.class, sa -> sa.getSlot().getName())
						.addBodyField("Window Start", OpenSlotAllocation.class, sa -> DateTimeFormatter.ofPattern(DateTimeFormatsProvider.INSTANCE.getDateStringDisplay()).format(sa.getSlot().getWindowStart()))
						.addBodyField("Window End", OpenSlotAllocation.class, sa -> DateTimeFormatter.ofPattern(DateTimeFormatsProvider.INSTANCE.getDateTimeStringDisplay()).format(sa.getSlot().getSchedulingTimeWindow().getEnd().toLocalDateTime()));
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
			if (event instanceof Journey || event instanceof NonShippedJourney) {
				return Optional.of(new JourneyEventTooltip(tooltip));
			} else if (event instanceof Idle || event instanceof PortVisit || event instanceof NonShippedIdle || event instanceof NonShippedSlotVisit) {
				return Optional.of(new PortEventTooltip(tooltip));
			}
		}
		if (scheduleEventData instanceof final PositionsSequenceElement positionsSequenceElement) {
			return Optional.of(new BuySellEventTooltip(tooltip, positionsSequenceElement));
		}
		return Optional.empty();
	}
	
	private String getIdleHeader(Event event) {
		String header =  "";
		if(event.getPort() != null)
			header += "At " + event.getPort().getName() + " ";
		
		boolean laden;
		if(event instanceof Idle i) {
			laden = i.isLaden();
		} else if (event instanceof NonShippedIdle nsi) {
			laden = nsi.isLaden();
		}
		else {
			return header;
		}
		
		if(laden)
			header += "(Laden idle)";
		else
			header += "(Ballast idle)";
		
		return header;
	}
	
	private List<String> getEventFooterText(Event event) {
		ArrayList<String> footerText = new ArrayList<>();
		ArrayList<String> line1Elements = new ArrayList<>();
		ArrayList<String> line2Elements = new ArrayList<>();
		
		DecimalFormat decimalFormat = new DecimalFormat("#.#");
		
		if(event instanceof SlotVisit sv) {
			int lateness = LatenessUtils.getLatenessInHours(sv);
			Integer pnl = getPnL(sv);
			
			if(lateness != 0)
				line1Elements.add("Late by " + LatenessUtils.formatLatenessHours(lateness));
			
			if(pnl != null && pnl != 0)
				line2Elements.add("P&L " + String.format("$%,d", pnl));
		}
		else if (event instanceof PortVisit pv) {
			int lateness = LatenessUtils.getLatenessInHours(pv);
			Integer pnl = getPnL(pv);
			
			if(lateness != 0)
				line1Elements.add("Late by " + LatenessUtils.formatLatenessHours(lateness));
			
			if(pnl != null && pnl != 0)
				line2Elements.add("P&L " + String.format("$%,d", pnl));
		}
		
		if(event instanceof Journey jEvent)
			line1Elements.add(decimalFormat.format(jEvent.getSpeed()) + " knots");
			
		// Add fuels
		if(event instanceof FuelUsage fEvent) {
			for(FuelQuantity fuel : fEvent.getFuels()) {
				if(fuel.getFuel() == Fuel.PILOT_LIGHT)
					line1Elements.add("Pilot light");
				else if(fuel.getFuel() == Fuel.BASE_FUEL)
					line1Elements.add("Base fuel");
				else
					line1Elements.add(fuel.getFuel().toString());
			}
		}
			
			
		// Add canal
		if(event instanceof Journey jEvent) {
			RouteOption route = jEvent.getRouteOption();
			if(route != null && route != RouteOption.DIRECT) {
				line1Elements.add(PortModelLabeller.getName(route));
			}
				
			final CanalBookingSlot canalBooking = jEvent.getCanalBooking();
			String direction = "";
			if (jEvent.getCanalEntrance() == CanalEntry.NORTHSIDE) {
				direction = "southbound";
			}
			if (jEvent.getCanalEntrance() == CanalEntry.SOUTHSIDE) {
				direction = "northbound";
			}

			if (canalBooking != null) {
				if(jEvent.getCanalDateTime() != null) {
					line2Elements.add(String.format("Canal booking: %s %s", DateTimeFormatsProvider.INSTANCE.createDateStringDisplayFormatter().format(jEvent.getCanalDateTime()), direction));
				}
			}
			else {
				if (jEvent.getCanalJourneyEvent() != null) {
					line2Elements.add(String.format("Panama wait %dd (of %dd)", jEvent.getCanalJourneyEvent().getPanamaWaitingTimeHours() / 24,
							jEvent.getCanalJourneyEvent().getMaxAvailablePanamaWaitingTimeHours() / 24));
				}
			}
		}	
		
		String line1 = "";
		for(int i = 0; i < line1Elements.size(); i++) {
			if (i != line1Elements.size() - 1)
				line1 += (line1Elements.get(i) + " | ");
			else
				line1 += line1Elements.get(i);
		}
		
		String line2 = "";
		for(int i = 0; i < line2Elements.size(); i++) {
			if (i != line2Elements.size() - 1)
				line2 += (line2Elements.get(i) + " | ");
			else
				line2 += line2Elements.get(i);
		}
			
		footerText.add(line1);
		footerText.add(line2);
		
		return footerText;
	}
	
	private Integer getPnL(final Object object) {
		ProfitAndLossContainer container = null;

		if (object instanceof CargoAllocation ca) {
			container = ca;
		}
		if (object instanceof SlotVisit slotVisit) {
			if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
				container = slotVisit.getSlotAllocation().getCargoAllocation();
			}
		}
		if (object instanceof VesselEventVisit vev) {
			container = vev;
		}
		if (object instanceof StartEvent evt) {
			container = evt;
		}
		if (object instanceof GeneratedCharterOut evt) {
			container = evt;
		}

		if (container != null) {
			final GroupProfitAndLoss dataWithKey = container.getGroupProfitAndLoss();
			if (dataWithKey != null) {
				return (int) dataWithKey.getProfitAndLoss();
			}
		}

		return null;

	}

}
