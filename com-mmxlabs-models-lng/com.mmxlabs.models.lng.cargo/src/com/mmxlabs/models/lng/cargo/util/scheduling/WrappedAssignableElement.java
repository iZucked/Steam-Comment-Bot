/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util.scheduling;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.util.CargoTravelTimeUtils;
import com.mmxlabs.models.lng.cargo.util.IAssignableElementDateProvider;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;

public class WrappedAssignableElement {

	private final AssignableElement assignableElement;
	private Port startPort;
	private Port endPort;
	private Pair<ZonedDateTime, ZonedDateTime> startWindow;
	private Pair<ZonedDateTime, ZonedDateTime> endWindow;
	private int minElementDuration;

	public WrappedAssignableElement(final @NonNull AssignableElement assignableElement, final @Nullable PortModel portModel, @Nullable ModelDistanceProvider modelDistanceProvider,
			@Nullable final IAssignableElementDateProvider dateProvider) {
		this.assignableElement = assignableElement;
		if (assignableElement instanceof Cargo) {
			final Cargo cargo = (Cargo) assignableElement;
			initFromCargo(cargo, portModel, modelDistanceProvider, dateProvider);
		} else if (assignableElement instanceof FakeCargo) {
			final FakeCargo fakeCargo = (FakeCargo) assignableElement;
			initFromCargo(fakeCargo, portModel, modelDistanceProvider, dateProvider);
		} else if (assignableElement instanceof VesselEvent) {
			final VesselEvent vesselEvent = (VesselEvent) assignableElement;
			initFromVesselEvent(vesselEvent);
		}
	}

	private void initFromVesselEvent(final VesselEvent vesselEvent) {
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

	private final List<Slot<?>> getSortedSlots(final AssignableElement cargo) {
		if (cargo instanceof Cargo) {
			return ((Cargo)cargo).getSortedSlots();
		}
		else if (cargo instanceof FakeCargo) {
			return ((FakeCargo)cargo).getSlots();
		}
		else {
			throw new IllegalArgumentException("Unsupport cargo type: "+cargo.getClass().getCanonicalName());
		}
	}
	
	private final int countSpotSlots(final List<Slot<?>> sortedSlots) {
		int i = 0;
		for (final Slot<?> slot : sortedSlots) {
			if (slot instanceof SpotSlot) {
				i++;
			}
		}
		return i;
	}
	
	private void initFromCargo(final AssignableElement cargo, final PortModel portModel, ModelDistanceProvider modelDistanceProvider,
			final IAssignableElementDateProvider dateProvider) {
		final List<Slot<?>> sortedSlots = getSortedSlots(cargo);
		assert sortedSlots != null;
		assert countSpotSlots(sortedSlots) <= 1;
		Slot<?> firstSlot = getFirstSlot(sortedSlots);
		Slot<?> lastSlot = getLastSlot(sortedSlots);
		
		final ZonedDateTime minEndDate = getMinEndDate(cargo, portModel, modelDistanceProvider, dateProvider, sortedSlots);
		final ZonedDateTime maxStartDate = getMaxStartDate(cargo, portModel, modelDistanceProvider, dateProvider, sortedSlots);

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
	}

	private ZonedDateTime getMinEndDate(final AssignableElement cargo, final PortModel portModel, ModelDistanceProvider modelDistanceProvider, final IAssignableElementDateProvider dateProvider,
			final List<Slot<?>> sortedSlots) {
		final ZonedDateTime minEndDate;
		Slot<?> lastSlot = null;
		ZonedDateTime lastTime = null;
		for (final Slot<?> slot : sortedSlots) {
			final Port nextPort = slot.getPort();
			final ZonedDateTime slotTime = getWindowStart(slot, dateProvider);
			if (lastSlot != null && lastSlot.getPort() != null && lastTime != null) {
				final int minTravelTime = portModel == null ? 0 : getTravelTime(cargo, portModel, lastSlot, slot, modelDistanceProvider);
				
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
			lastSlot = slot;
		}
		minEndDate = lastTime;
		return minEndDate;
	}

	private Slot<?> getFirstSlot(List<Slot<?>> sortedSlots) {
		// TODO: Handle LDD etc
		for (final Slot<?> slot : sortedSlots) {
			if (slot instanceof SpotSlot) {
				continue;
			}
			else {
				return slot;
			}
		}
		return null;
	}

	private Slot<?> getLastSlot(List<Slot<?>> sortedSlots) {
		Slot<?> lastSlot = null;
		// TODO: Handle LDD etc
		for (final Slot<?> slot : sortedSlots) {
			if (slot instanceof SpotSlot) {
				continue;
			}
			lastSlot = slot;
		}
		return lastSlot;
	}

	private ZonedDateTime getMaxStartDate(final AssignableElement cargo, final PortModel portModel, ModelDistanceProvider modelDistanceProvider, final IAssignableElementDateProvider dateProvider,
			final List<Slot<?>> sortedSlots) {
		final ZonedDateTime maxStartDate;
		final List<Slot<?>> reverseOrder = new ArrayList<>(sortedSlots);
		Collections.reverse(reverseOrder);
		Slot<?> lastSlot = null;
		ZonedDateTime lastTime = null;
		
		for (final Slot<?> slot : reverseOrder) {
			final ZonedDateTime slotTime = getWindowEnd(slot, dateProvider);
			if (lastSlot != null && lastSlot.getPort() != null && lastTime != null) {
				final int minTravelTime = portModel == null ? 0 : getTravelTime(cargo, portModel, slot, lastSlot, modelDistanceProvider);
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
			lastSlot = slot;
		}
		maxStartDate = lastTime;
		return maxStartDate;
	}
	
	private @NonNull ZonedDateTime getWindowStart(@NonNull final Slot<?> slot, @Nullable final IAssignableElementDateProvider dateProvider) {
		if (dateProvider != null) {
			ZonedDateTime date = dateProvider.getSlotWindowStart(slot);
			if (date != null) {
				return date;
			}
		}
		return slot.getSchedulingTimeWindow().getStart();
	}

	private @NonNull ZonedDateTime getWindowEnd(@NonNull final Slot<?> slot, @Nullable final IAssignableElementDateProvider dateProvider) {
		if (dateProvider != null) {
			ZonedDateTime date = dateProvider.getSlotWindowEnd(slot);
			if (date != null) {
				return date;
			}
		}
		return slot.getSchedulingTimeWindow().getEnd();
	}

	private int getDurationInHours(@NonNull final Slot<?> slot, @Nullable final IAssignableElementDateProvider dateProvider) {

		if (dateProvider != null) {
			OptionalInt duration = dateProvider.getSlotDurationInHours(slot);
			if (duration.isPresent()) {
				return duration.getAsInt();
			}
		}
		return slot.getSchedulingTimeWindow().getDuration();
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

	private int getTravelTime(final AssignableElement assignableElement, final PortModel portModel, final Slot<?> fromSlot, final Slot<?> toSlot, ModelDistanceProvider modelDistanceProvider) {
		Vessel vessel = null;
		
		final VesselAssignmentType vesselAssignmentType = assignableElement.getVesselAssignmentType();
		if (vesselAssignmentType instanceof VesselAvailability) {
			final VesselAvailability vesselAvailability = (VesselAvailability) vesselAssignmentType;
			vessel = vesselAvailability.getVessel();
		} else if (vesselAssignmentType instanceof CharterInMarket) {
			final CharterInMarket charterInMarket = (CharterInMarket) vesselAssignmentType;
			vessel = charterInMarket.getVessel();
		} else if (vesselAssignmentType instanceof CharterInMarketOverride) {
			final CharterInMarketOverride charterInMarketOverride = (CharterInMarketOverride) vesselAssignmentType;
			vessel = charterInMarketOverride.getCharterInMarket().getVessel();
		}

		return getTravelTime(vessel, portModel, fromSlot, toSlot, modelDistanceProvider);
	}

	private int getTravelTime(final @Nullable Vessel vessel, final PortModel portModel, final Slot<?> from, final Slot<?> to, ModelDistanceProvider modelDistanceProvider) {
		if (vessel == null) {
			return Integer.MAX_VALUE;
		}

		final double maxSpeed = vessel.getVesselOrDelegateMaxSpeed();
		if (maxSpeed == 0.0) {
			return Integer.MAX_VALUE;
		}

		int travelTimeInHours = Integer.MAX_VALUE;
		if (portModel != null) {
			for (final Route route : portModel.getRoutes()) {
				int totalTime = CargoTravelTimeUtils.getTimeForRoute(vessel, maxSpeed, route.getRouteOption(), from, to, modelDistanceProvider);
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
