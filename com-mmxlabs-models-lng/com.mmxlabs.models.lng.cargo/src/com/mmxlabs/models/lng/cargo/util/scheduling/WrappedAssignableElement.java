/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util.scheduling;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.util.IAssignableElementDateProvider;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.fleet.util.TravelTimeUtils;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.util.RouteDistanceLineCache;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;

public class WrappedAssignableElement {

	private final AssignableElement assignableElement;
	private Port startPort;
	private Port endPort;
	private Pair<ZonedDateTime, ZonedDateTime> startWindow;
	private Pair<ZonedDateTime, ZonedDateTime> endWindow;
	private int minElementDuration;

	public WrappedAssignableElement(final @NonNull AssignableElement assignableElement, final @Nullable PortModel portModel, @Nullable final IAssignableElementDateProvider dateProvider) {
		this.assignableElement = assignableElement;
		if (assignableElement instanceof Cargo) {
			final Cargo cargo = (Cargo) assignableElement;

			final List<@NonNull Slot> sortedSlots = cargo.getSortedSlots();
			assert sortedSlots != null;
			Slot firstSlot = null;
			Slot lastSlot = null;

			// TODO: Handle LDD etc
			for (final Slot slot : sortedSlots) {
				if (slot instanceof SpotSlot) {
					continue;
				}
				if (firstSlot == null) {
					firstSlot = slot;
				}
				lastSlot = slot;
			}

			final ZonedDateTime minEndDate;
			{
				Port lastPort = null;
				ZonedDateTime lastTime = null;
				for (final Slot slot : sortedSlots) {

					final Port nextPort = slot.getPort();
					final ZonedDateTime slotTime = getWindowStart(slot, dateProvider);
					if (lastPort != null && lastTime != null) {
						final int minTravelTime = portModel == null ? 0 : getTravelTime(assignableElement, portModel, lastPort, nextPort);
						if (minTravelTime == Integer.MAX_VALUE) {
							lastTime = slotTime;

						} else {
							final ZonedDateTime nextTime = lastTime.plusHours(minTravelTime);

							if (nextTime.isAfter(slotTime)) {
								lastTime = nextTime;
							} else {
								lastTime = slotTime;
							}
						}
					} else {
						lastTime = slotTime;
					}

					// Add on slot duration
					final int duration = getDurationInHours(slot, dateProvider);

					lastTime = lastTime.plusHours(duration);
					lastPort = slot.getPort();
				}
				minEndDate = lastTime;
			}
			final ZonedDateTime maxStartDate;
			{
				final List<@NonNull Slot> reverseOrder = new ArrayList<>(sortedSlots);
				Collections.reverse(reverseOrder);

				Port lastPort = null;
				ZonedDateTime lastTime = null;
				for (final Slot slot : reverseOrder) {

					final ZonedDateTime slotTime = getWindowEnd(slot, dateProvider);
					final Port nextPort = slot.getPort();
					if (lastPort != null && lastTime != null) {
						final int minTravelTime = portModel == null ? 0 : getTravelTime(assignableElement, portModel, nextPort, lastPort);
						if (minTravelTime == Integer.MAX_VALUE) {
							lastTime = slotTime;

						} else {
							// latest Departure time
							ZonedDateTime nextTime = lastTime.minusHours(minTravelTime);

							final int duration = getDurationInHours(slot, dateProvider);

							nextTime = nextTime.minusHours(duration);

							if (nextTime.isAfter(slotTime)) {
								lastTime = slotTime;
							} else {
								lastTime = nextTime;
							}
						}
					} else {
						lastTime = slotTime;
					}

					// Add on slot duration
					lastPort = slot.getPort();
				}
				maxStartDate = lastTime;
			}

			this.startPort = firstSlot.getPort();
			this.endPort = lastSlot.getPort();
			final int lastSlotDurationInHours = getDurationInHours(lastSlot, dateProvider);

			this.startWindow = new Pair<>(getWindowStart(firstSlot, dateProvider), getWindowEnd(firstSlot, dateProvider));
			this.endWindow = new Pair<>(getWindowStart(lastSlot, dateProvider).plusHours(lastSlotDurationInHours), getWindowEnd(lastSlot, dateProvider).plusHours(lastSlotDurationInHours));

			// Update the end window based on travel time
			if (endWindow.getFirst().isBefore(minEndDate)) {
				endWindow.setFirst(minEndDate);
			}
			if (endWindow.getSecond().isBefore(minEndDate)) {
				endWindow.setSecond(minEndDate);
			}

			if (startWindow.getSecond().isAfter(maxStartDate)) {
				startWindow.setSecond(maxStartDate);
			}
			if (startWindow.getFirst().isAfter(maxStartDate)) {
				// assert false;
				startWindow.setFirst(maxStartDate);
			}

			final int ladenDuration = Hours.between(startWindow.getSecond(), endWindow.getFirst());
			this.minElementDuration = ladenDuration + lastSlotDurationInHours;

		} else if (assignableElement instanceof VesselEvent) {
			final VesselEvent vesselEvent = (VesselEvent) assignableElement;

			this.startPort = vesselEvent.getPort();
			this.endPort = vesselEvent.getPort();
			if (vesselEvent instanceof CharterOutEvent) {
				final CharterOutEvent charterOutEvent = (CharterOutEvent) vesselEvent;
				if (charterOutEvent.isSetRelocateTo()) {
					this.endPort = charterOutEvent.getRelocateTo();
				}
			}

			this.startWindow = new Pair<>(vesselEvent.getStartAfterAsDateTime(), vesselEvent.getStartByAsDateTime());
			this.endWindow = new Pair<>(vesselEvent.getStartAfterAsDateTime().plusDays(vesselEvent.getDurationInDays()), vesselEvent.getStartByAsDateTime().plusDays(vesselEvent.getDurationInDays()));

			// TODO: Correct? What about day light savings!
			this.minElementDuration = vesselEvent.getDurationInDays() * 24;
		}
	}

	private @NonNull ZonedDateTime getWindowStart(@NonNull final Slot slot, @Nullable final IAssignableElementDateProvider dateProvider) {

		if (dateProvider != null) {
			ZonedDateTime date = dateProvider.getSlotWindowStart(slot);
			if (date != null) {
				return date;
			}
		}
		return slot.getWindowStartWithSlotOrPortTime();
	}

	private @NonNull ZonedDateTime getWindowEnd(@NonNull final Slot slot, @Nullable final IAssignableElementDateProvider dateProvider) {

		if (dateProvider != null) {
			ZonedDateTime date = dateProvider.getSlotWindowEnd(slot);
			if (date != null) {
				return date;
			}
		}
		return slot.getWindowEndWithSlotOrPortTime();
	}

	private int getDurationInHours(@NonNull final Slot slot, @Nullable final IAssignableElementDateProvider dateProvider) {

		if (dateProvider != null) {
			OptionalInt duration = dateProvider.getSlotDurationInHours(slot);
			if (duration.isPresent()) {
				return duration.getAsInt();
			}
		}
		return slot.getSlotOrPortDuration();
	}

	public boolean isCargo() {
		return assignableElement instanceof Cargo;
	}

	public boolean isVesselEvent() {
		return assignableElement instanceof VesselEvent;
	}

	public Port getStartPort() {
		return this.startPort;
	}

	public Port getEndPort() {
		return this.endPort;
	}

	public Pair<ZonedDateTime, ZonedDateTime> getStartWindow() {
		return startWindow;
	}

	public Pair<ZonedDateTime, ZonedDateTime> getEndWindow() {
		return endWindow;
	}

	private int getTravelTime(final AssignableElement assignableElement, final PortModel portModel, final Port from, final Port to) {

		VesselClass vesselClass = null;

		final VesselAssignmentType vesselAssignmentType = assignableElement.getVesselAssignmentType();
		if (vesselAssignmentType instanceof VesselAvailability) {
			final VesselAvailability vesselAvailability = (VesselAvailability) vesselAssignmentType;
			final Vessel vessel = vesselAvailability.getVessel();
			if (vessel != null) {
				vesselClass = vessel.getVesselClass();
			}
		} else if (vesselAssignmentType instanceof CharterInMarket) {
			final CharterInMarket charterInMarket = (CharterInMarket) vesselAssignmentType;
			vesselClass = charterInMarket.getVesselClass();
		}

		if (vesselClass == null) {
			return Integer.MAX_VALUE;
		}

		final double maxSpeed = vesselClass.getMaxSpeed();
		if (maxSpeed == 0.0) {
			return Integer.MAX_VALUE;
		}

		int travelTimeInHours = Integer.MAX_VALUE;
		if (portModel != null) {
			for (final Route route : portModel.getRoutes()) {
				int totalTime = TravelTimeUtils.getTimeForRoute(vesselClass, maxSpeed, route, from, to);
				if (totalTime != Integer.MAX_VALUE) {
					travelTimeInHours = Math.min(totalTime, travelTimeInHours);
				}
			}
		}
		return travelTimeInHours;

	}

	public int getMinEventDurationInHours() {
		return minElementDuration;
	}

	public AssignableElement getAssignableElement() {
		return assignableElement;
	}

	public int getSequenceHint() {
		return assignableElement.getSequenceHint();
	}
}
