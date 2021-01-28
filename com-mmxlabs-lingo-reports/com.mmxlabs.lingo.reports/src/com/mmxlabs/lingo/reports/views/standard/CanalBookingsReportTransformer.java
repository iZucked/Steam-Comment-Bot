/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.port.CanalEntry;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.PanamaBookingPeriod;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.scenario.service.ScenarioResult;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

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
		public final @Nullable LocalDateTime bookingDate;
		public final @Nullable String entryPointName;
		public final @Nullable PortVisit event;
		public final RouteOption routeOption;
		public final CanalBookingSlot booking;
		public final String period;
		public final Slot nextSlot;
		public final String notes;
		public boolean warn;
		public boolean pinned;

		public RowData(final @NonNull String scheduleName, boolean pinned, final boolean preBooked, final CanalBookingSlot booking, final RouteOption routeOption,
				final @NonNull LocalDateTime bookingDate, final @NonNull String entryPointName, final @Nullable PortVisit usedSlot, final String period, final @Nullable Slot nextSlot,
				@Nullable final String notes) {
			super();
			this.scheduleName = scheduleName;
			this.pinned = pinned;
			this.preBooked = preBooked;
			this.booking = booking;
			this.routeOption = routeOption;
			this.bookingDate = bookingDate;
			this.entryPointName = entryPointName;
			this.event = usedSlot;
			this.period = period;
			this.nextSlot = nextSlot;
			this.notes = notes == null ? "" : notes;
			this.warn = false;

			this.dummy = false;
		}

		public RowData() {
			super();
			this.scheduleName = "";
			this.pinned = false;
			this.booking = null;
			this.bookingDate = null;
			this.entryPointName = null;
			this.event = null;
			this.routeOption = null;
			this.preBooked = false;
			this.period = "";
			this.nextSlot = null;
			this.warn = false;
			this.notes = "";
			this.dummy = true;
		}
	}

	@NonNull
	public List<RowData> transform(@NonNull final Schedule schedule, @NonNull final ScenarioResult scenarioResult, boolean pinned) {

		final ScenarioModelRecord modelRecord = scenarioResult.getModelRecord();

		final LNGScenarioModel scenarioModel = scenarioResult.getTypedRoot(LNGScenarioModel.class);
		assert scenarioModel != null;

		final ModelDistanceProvider modelDistanceProvider = scenarioResult.getScenarioDataProvider().getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES, ModelDistanceProvider.class);

		final Set<CanalBookingSlot> existingBookings = new LinkedHashSet<>();
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioModel);
		if (cargoModel.getCanalBookings() != null) {
			cargoModel.getCanalBookings().getCanalBookingSlots().forEach(booking -> existingBookings.add(booking));
		}

		final List<RowData> result = new LinkedList<>();

		final List<RowData> relaxedSouthbound = new LinkedList<>();
		final List<RowData> relaxedNorthbound = new LinkedList<>();

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

					Event nextEvent = evt.getNextEvent();
					Slot nextSlot = null;
					while (nextEvent != null) {
						if (nextEvent instanceof SlotVisit) {
							final SlotVisit nextSlotVisit = (SlotVisit) nextEvent;
							final Slot slot = nextSlotVisit.getSlotAllocation().getSlot();
							if (slot != null) {
								nextSlot = slot;
							}
							break;
						}
						nextEvent = nextEvent.getNextEvent();
					}

					if (journey.getCanalBooking() != null) {
						final CanalBookingSlot booking = journey.getCanalBooking();
						final String entryPointName = modelDistanceProvider.getCanalEntranceName(journey.getRouteOption(), booking.getCanalEntrance());
						result.add(new RowData(modelRecord.getName(), pinned, true, booking, journey.getRouteOption(), booking.getBookingDate().atTime(3, 0), entryPointName,
								(PortVisit) journey.getPreviousEvent(), period, nextSlot, booking.getNotes()));
						existingBookings.remove(booking);
					} else if (journey.getRouteOption() == RouteOption.PANAMA) {
						final String entryPointName = modelDistanceProvider.getCanalEntranceName(journey.getRouteOption(), journey.getCanalEntrance());

						final RowData rowData = new RowData(modelRecord.getName(), pinned, false, null, journey.getRouteOption(), journey.getCanalDateTime(), entryPointName,
								(PortVisit) journey.getPreviousEvent(), period, nextSlot, "");
						if (journey.getCanalBookingPeriod() == PanamaBookingPeriod.RELAXED) {
							if (journey.getCanalEntrance() == CanalEntry.NORTHSIDE) {
								relaxedSouthbound.add(rowData);
							} else {
								relaxedNorthbound.add(rowData);
							}
						}
						result.add(rowData);
					}
				}
			}
		}
		if (cargoModel.getCanalBookings() != null) {
			// if (relaxedNorthbound.size() > cargoModel.getCanalBookings().getFlexibleBookingAmountNorthbound()) {
			// relaxedNorthbound.forEach(d -> d.warn = true);
			// }
			if (relaxedSouthbound.size() > cargoModel.getCanalBookings().getFlexibleBookingAmountSouthbound()) {
				relaxedSouthbound.forEach(d -> d.warn = true);
			}
		}
		// unused options
		for (final CanalBookingSlot booking : existingBookings) {
			final String entryPointName = modelDistanceProvider.getCanalEntranceName(booking.getRouteOption(), booking.getCanalEntrance());
			result.add(new RowData(modelRecord.getName(), pinned, true, booking, booking.getRouteOption(), booking.getBookingDate().atTime(3, 0), entryPointName, null, "Unused", null,
					booking.getNotes()));
		}

		return result;
	}
}
