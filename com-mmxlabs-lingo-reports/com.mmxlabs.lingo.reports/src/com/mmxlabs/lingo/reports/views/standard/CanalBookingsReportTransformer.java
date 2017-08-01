/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
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
		public final @Nullable String entryPointName;
		public final @Nullable PortVisit event;
		public final RouteOption routeOption;
		public final CanalBookingSlot booking;
		public final String period;

		public RowData(final @NonNull String scheduleName, final boolean preBooked, final CanalBookingSlot booking, final RouteOption routeOption, final @NonNull LocalDate bookingDate,
				final @NonNull String entryPointName, final @Nullable PortVisit usedSlot, final String period) {
			super();
			this.scheduleName = scheduleName;
			this.preBooked = preBooked;
			this.booking = booking;
			this.routeOption = routeOption;
			this.bookingDate = bookingDate;
			this.entryPointName = entryPointName;
			this.event = usedSlot;
			this.period = period;

			this.dummy = false;
		}

		public RowData() {
			super();
			this.scheduleName = "";
			this.booking = null;
			this.bookingDate = null;
			this.entryPointName = null;
			this.event = null;
			this.routeOption = null;
			this.preBooked = false;
			this.period = "";

			this.dummy = true;
		}
	}

	@NonNull
	public List<RowData> transform(@NonNull final Schedule schedule, @NonNull final ScenarioResult scenarioResult) {

		final ScenarioModelRecord modelRecord = scenarioResult.getModelRecord();

		final LNGScenarioModel scenarioModel = scenarioResult.getTypedRoot(LNGScenarioModel.class);
		assert scenarioModel != null;

		final ModelDistanceProvider modelDistanceProvider = scenarioResult.getScenarioDataProvider().getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES, ModelDistanceProvider.class);

		final LocalDate promptDate = scenarioModel.getPromptPeriodStart();
		LocalDate strictDate;
		LocalDate relaxedDate;

		final Set<CanalBookingSlot> existingBookings = new LinkedHashSet<>();
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioModel);
		if (cargoModel.getCanalBookings() != null) {
			cargoModel.getCanalBookings().getCanalBookingSlots().forEach(booking -> existingBookings.add(booking));
			strictDate = promptDate.plusDays(cargoModel.getCanalBookings().getStrictBoundaryOffsetDays());
			relaxedDate = promptDate.plusDays(cargoModel.getCanalBookings().getRelaxedBoundaryOffsetDays());
		} else {
			strictDate = promptDate;
			relaxedDate = promptDate;
		}

		final List<RowData> result = new LinkedList<>();

		for (final Sequence seq : schedule.getSequences()) {

			for (final Event evt : seq.getEvents()) {

				if (evt instanceof Journey) {
					final Journey journey = (Journey) evt;

					String period = "";

					switch (journey.getCanalBookingPeriod()) {
					case BEYOND:
						period = "Open";
						break;
					case NOMINAL:
						period = "Nominal";
						break;
					case RELAXED:
						period = "Relaxed";
						break;
					case STRICT:
						period = "Strict";
						break;
					default:
						period = "Open";
						break;

					}
					//
					// if (evt.getSequence() != null && evt.getSequence().getSequenceType() == SequenceType.ROUND_TRIP) {
					// period = "Nominal";
					// } else if (relaxedDate == null || strictDate == null || journey.getStart() == null) {
					// period = "Open";
					// } else if (journey.getStart().isAfter(relaxedDate.atStartOfDay(ZoneId.of("UTC")))) {
					// period = "Open";
					// } else if (journey.getStart().isAfter(strictDate.atStartOfDay(ZoneId.of("UTC")))) {
					// period = "Relaxed";
					// } else {
					// period = "Strict";
					// }

					if (journey.getCanalBooking() != null) {
						final CanalBookingSlot booking = journey.getCanalBooking();
						final String entryPointName = modelDistanceProvider.getCanalEntranceName(journey.getRouteOption(), booking.getCanalEntrance());
						result.add(
								new RowData(modelRecord.getName(), true, booking, journey.getRouteOption(), booking.getBookingDate(), entryPointName, (PortVisit) journey.getPreviousEvent(), period));
						existingBookings.remove(booking);
					} else if (journey.getRouteOption() == RouteOption.PANAMA) {
						final String entryPointName = modelDistanceProvider.getCanalEntranceName(journey.getRouteOption(), journey.getCanalEntrance());
						result.add(new RowData(modelRecord.getName(), false, null, journey.getRouteOption(), journey.getCanalDate(), entryPointName, (PortVisit) journey.getPreviousEvent(), period));
					}
				}
			}
		}

		// unused options
		for (final CanalBookingSlot booking : existingBookings) {
			final String entryPointName = modelDistanceProvider.getCanalEntranceName(booking.getRouteOption(), booking.getCanalEntrance());
			result.add(new RowData(modelRecord.getName(), true, booking, booking.getRouteOption(), booking.getBookingDate(), entryPointName, null, "Unused"));
		}

		return result;
	}
}
