/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselGroup;
import com.mmxlabs.models.lng.port.CanalEntry;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.util.PortModelLabeller;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.PanamaBookingPeriod;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.types.util.SetUtils;
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
		public final @Nullable String bookingCode;
		public final @Nullable String entryPointName;
		public final @Nullable PortVisit event;
		public final RouteOption routeOption;
		public final CanalBookingSlot booking;
		public final String type;
		public final Slot<?> nextSlot;
		public final String notes;
		public final boolean warn;
		public final boolean pinned;
		public final @Nullable String vessel;

		public RowData(final @NonNull String scheduleName, final boolean pinned, final boolean preBooked, final CanalBookingSlot booking, final RouteOption routeOption,
				final @NonNull LocalDateTime bookingDate, final @NonNull String entryPointName, final @Nullable PortVisit usedSlot, final String type, final @Nullable Slot<?> nextSlot,
				@Nullable final String notes, @Nullable final String bookingCode, @Nullable final String vessel) {
			super();
			this.scheduleName = scheduleName;
			this.pinned = pinned;
			this.preBooked = preBooked;
			this.booking = booking;
			this.routeOption = routeOption;
			this.bookingDate = bookingDate;
			this.entryPointName = entryPointName;
			this.event = usedSlot;
			this.type = type;
			this.nextSlot = nextSlot;
			this.notes = notes == null ? "" : notes;
			this.bookingCode = bookingCode == null ? "" : bookingCode;
			this.vessel = vessel == null ? "" : vessel;

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
			this.type = "";
			this.nextSlot = null;
			this.warn = false;
			this.notes = "";
			this.bookingCode = "";
			this.vessel = "";
			this.dummy = true;
		}
	}

	@NonNull
	public List<RowData> transform(@NonNull final Schedule schedule, @NonNull final ScenarioResult scenarioResult, final boolean pinned) {

		final ScenarioModelRecord modelRecord = scenarioResult.getModelRecord();

		final LNGScenarioModel scenarioModel = scenarioResult.getTypedRoot(LNGScenarioModel.class);
		assert scenarioModel != null;

		final Set<CanalBookingSlot> existingBookings = new LinkedHashSet<>();
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioModel);
		if (cargoModel.getCanalBookings() != null) {
			cargoModel.getCanalBookings().getCanalBookingSlots().forEach(existingBookings::add);
		}

		final List<RowData> result = new LinkedList<>();

		final List<RowData> relaxedSouthbound = new LinkedList<>();
		final List<RowData> relaxedNorthbound = new LinkedList<>();

		for (final Sequence seq : schedule.getSequences()) {

			for (final Event evt : seq.getEvents()) {

				if (evt instanceof final Journey journey) {

					String type = "";

					if (journey.getCanalBooking() != null) {
						type = "Booked";
					} else {
						if (journey.getCanalJourneyEvent() != null) {
							type = "Wait (" + (journey.getCanalJourneyEvent().getPanamaWaitingTimeHours() / 24) + "d)";
						} else {
							type = "Wait";
						}
					}

					Event nextEvent = evt.getNextEvent();
					Slot<?> nextSlot = null;
					while (nextEvent != null) {
						if (nextEvent instanceof final SlotVisit nextSlotVisit) {
							final Slot<?> slot = nextSlotVisit.getSlotAllocation().getSlot();
							if (slot != null) {
								nextSlot = slot;
							}
							break;
						}
						nextEvent = nextEvent.getNextEvent();
					}

					if (journey.getCanalBooking() != null) {
						final CanalBookingSlot booking = journey.getCanalBooking();
						final String canalTravelDirection = PortModelLabeller.getDirection(booking.getCanalEntrance());
						result.add(new RowData(modelRecord.getName(), pinned, true, booking, journey.getRouteOption(), booking.getBookingDate().atTime(3, 0), canalTravelDirection,
								(PortVisit) journey.getPreviousEvent(), type, nextSlot, booking.getNotes(), getBookingCode(booking), getVessel(booking, journey)));
						existingBookings.remove(booking);
					} else if (journey.getRouteOption() == RouteOption.PANAMA) {
						final String canalTravelDirection = PortModelLabeller.getDirection(journey.getCanalEntrance());
						final String vesselName = getVesselName(journey);
						final String bookingCode = getVesselBookingCode(journey, cargoModel.getCanalBookings());
						final RowData rowData = new RowData(modelRecord.getName(), pinned, false, null, journey.getRouteOption(), journey.getCanalDateTime(), canalTravelDirection,
								(PortVisit) journey.getPreviousEvent(), type, nextSlot, "", bookingCode, vesselName);
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

		// unused options
		for (final CanalBookingSlot booking : existingBookings) {
			final String canalTravelDirection = PortModelLabeller.getDirection(booking.getCanalEntrance());
			if (booking.getBookingDate() != null) {
				result.add(new RowData(modelRecord.getName(), pinned, true, booking, booking.getRouteOption(), booking.getBookingDate().atTime(3, 0), canalTravelDirection, null, "Unused", null,
						booking.getNotes(), getBookingCode(booking), getVessel(booking, null)));
			}
		}

		return result;
	}

	private String getVesselBookingCode(final Journey journey, final CanalBookings canalBookings) {
		Vessel v = null;
		String defaultGroupName = "";
		if (journey.getSequence() != null && canalBookings != null) {
			if (journey.getSequence().getVesselCharter() != null) {
				v = journey.getSequence().getVesselCharter().getVessel();
			} else if (journey.getSequence().getCharterInMarket() != null) {
				v = journey.getSequence().getCharterInMarket().getVessel();
			}

			if (v != null) {
				for (final var vgp : canalBookings.getVesselGroupCanalParameters()) {
					if (vgp.getVesselGroup() == null || vgp.getVesselGroup().isEmpty()) {
						defaultGroupName = vgp.getName();
					}
					final Set<Vessel> vgvs = SetUtils.getObjects(vgp.getVesselGroup());
					if (vgvs.contains(v)) {
						return vgp.getName();
					}
				}
			}
		}
		return defaultGroupName;
	}

	private String getVesselName(final Journey journey) {
		if (journey.getSequence() != null) {
			if (journey.getSequence().getVesselCharter() != null && journey.getSequence().getVesselCharter().getVessel() != null) {
				return journey.getSequence().getVesselCharter().getVessel().getName();
			} else if (journey.getSequence().getCharterInMarket() != null) {
				return journey.getSequence().getCharterInMarket().getName();
			}
		}
		// Else we don't know yet?
		return "";
	}

	private String getVessel(final CanalBookingSlot booking, final @Nullable Journey journey) {
		if (booking.getVessel() instanceof Vessel || booking.getVessel() instanceof VesselGroup) {
			return booking.getVessel().getName();
		} else if (journey != null) {
			return getVesselName(journey);
		}
		return "";
	}

	private String getBookingCode(final CanalBookingSlot booking) {
		if (booking.getBookingCode() != null && booking.getBookingCode().getName() != null) {
			return booking.getBookingCode().getName();
		}
		return "";
	}
}
