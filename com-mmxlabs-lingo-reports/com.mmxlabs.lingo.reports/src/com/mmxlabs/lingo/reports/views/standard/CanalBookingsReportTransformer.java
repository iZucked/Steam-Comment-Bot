/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.port.EntryPoint;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

/**
 * 
 * @author Simon Goodall
 * 
 */
public class CanalBookingsReportTransformer {
	public static class RowData {
		public final boolean dummy;
		public final boolean preBooked;
		public final String scheduleName;
		public final @Nullable LocalDate bookingDate;
		public final @Nullable EntryPoint entryPoint;
		public final @Nullable PortVisit event;
		public final RouteOption routeOption;
		public final String period;

		public RowData(final @NonNull String scheduleName, boolean preBooked, RouteOption routeOption, final @NonNull LocalDate bookingDate, final @NonNull EntryPoint entryPoint,
				final @Nullable PortVisit usedSlot, String period) {
			super();
			this.scheduleName = scheduleName;
			this.preBooked = preBooked;
			this.routeOption = routeOption;
			this.bookingDate = bookingDate;
			this.entryPoint = entryPoint;
			this.event = usedSlot;
			this.period = period;

			this.dummy = false;
		}

		public RowData() {
			super();
			this.scheduleName = "";
			this.bookingDate = null;
			this.entryPoint = null;
			this.event = null;
			this.routeOption = null;
			this.preBooked = false;
			this.period = "";

			this.dummy = true;
		}
	}

	@NonNull
	public List<RowData> transform(@NonNull final Schedule schedule, @NonNull final ScenarioResult scenarioResult) {

		ScenarioInstance scenarioInstance = scenarioResult.getScenarioInstance();

		LNGScenarioModel scenarioModel = scenarioResult.getTypedRoot(LNGScenarioModel.class);
		assert scenarioModel != null;

		LocalDate promptDate = scenarioModel.getPromptPeriodStart();
		LocalDate strictDate;
		LocalDate relaxedDate;

		Set<CanalBookingSlot> existingBookings = new LinkedHashSet<>();
		CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioModel);
		if (cargoModel.getCanalBookings() != null) {
			cargoModel.getCanalBookings().getCanalBookingSlots().forEach(booking -> existingBookings.add(booking));
			strictDate = promptDate;
			relaxedDate = promptDate;
		} else {
			strictDate = promptDate.plusDays(cargoModel.getCanalBookings().getStrictBoundaryOffsetDays());
			relaxedDate = promptDate.plusDays(cargoModel.getCanalBookings().getRelaxedBoundaryOffsetDays());
		}

		List<RowData> result = new LinkedList<>();

		for (final Sequence seq : schedule.getSequences()) {

			for (final Event evt : seq.getEvents()) {

				if (evt instanceof Journey) {
					Journey journey = (Journey) evt;

					String period = "";
					if (evt.getSequence() != null && evt.getSequence().getSequenceType() == SequenceType.ROUND_TRIP) {
						period = "Nominal";
					} else if (journey.getStart().isAfter(relaxedDate.atStartOfDay(ZoneId.of("UTC")))) {
						period = "Open";
					} else if (journey.getStart().isAfter(strictDate.atStartOfDay(ZoneId.of("UTC")))) {
						period = "Relaxed";
					} else {
						period = "Strict";
					}

					if (journey.getCanalBooking() != null) {
						CanalBookingSlot booking = journey.getCanalBooking();
						result.add(new RowData(scenarioInstance.getName(), true, journey.getRoute().getRouteOption(), booking.getBookingDate(), booking.getEntryPoint(),
								(PortVisit) journey.getPreviousEvent(), period));
						existingBookings.remove(booking);
					} else if (journey.getRoute() != null && journey.getRoute().getRouteOption() == RouteOption.PANAMA) {
						result.add(new RowData(scenarioInstance.getName(), false, journey.getRoute().getRouteOption(), journey.getCanalDate(), journey.getCanalEntry(),
								(PortVisit) journey.getPreviousEvent(), period));
					}
				}
			}
		}

		// unused options
		for (CanalBookingSlot booking : existingBookings) {
			result.add(new RowData(scenarioInstance.getName(), true, booking.getRoute().getRouteOption(), booking.getBookingDate(), booking.getEntryPoint(), null, "Unused"));
		}

		return result;
	}
}
