package com.mmxlabs.models.lng.cargo.util.scheduling;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.Platform;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
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

	public WrappedAssignableElement(final AssignableElement assignableElement, 
			/* final ActualsModel actualsModel, */ 
			final PortModel portModel) {
		this.assignableElement = assignableElement;
		if (assignableElement instanceof Cargo) {
			final Cargo cargo = (Cargo) assignableElement;

//			CargoActuals actuals = null;
//			Map<Slot, SlotActuals> slotActualMap = new HashMap<>();
//			// Is there a cargo actuals?
//			if (actualsModel != null) {
//				for (CargoActuals ca : actualsModel.getCargoActuals()) {
//					if (ca.getCargo() == cargo) {
//						actuals = ca;
//						ca.getActuals().forEach(sa -> slotActualMap.put(sa.getSlot(), sa));
//						break;
//					}
//
//				}
//			}
//			
			final List<Slot> sortedSlots = cargo.getSortedSlots();
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
					ZonedDateTime slotTime = slot.getWindowStartWithSlotOrPortTime();
//					if (slotActualMap.containsKey(slot)) {
//						slotTime = slotActualMap.get(slot).getOperationsStartAsDateTime();
//					}
					if (lastPort != null && lastTime != null) {
						final int minTravelTime = portModel == null ? 0 : getTravelTime(assignableElement, portModel, lastPort, nextPort);

						final ZonedDateTime nextTime = lastTime.plusHours(minTravelTime);

						if (nextTime.isAfter(slotTime)) {
							lastTime = nextTime;
						} else {
							lastTime = slotTime;
						}
					} else {
						lastTime = slotTime;
					}

					// Add on slot duration
					int duration = slot.getDuration();
//					if (slotActualMap.containsKey(slot)) {
//						SlotActuals slotActuals = slotActualMap.get(slot);
//						duration = Hours.between(slotActuals.getOperationsStartAsDateTime(), slotActuals.getOperationsEndAsDateTime());
//					}

					lastTime = lastTime.plusHours(duration);
					lastPort = slot.getPort();
				}
				minEndDate = lastTime;
			}
			final ZonedDateTime maxStartDate;
			{
				final List<Slot> reverseOrder = new ArrayList<>(sortedSlots);
				Collections.reverse(reverseOrder);

				Port lastPort = null;
				ZonedDateTime lastTime = null;
				for (final Slot slot : reverseOrder) {

					ZonedDateTime slotTime = slot.getWindowEndWithSlotOrPortTime();
//					if (slotActualMap.containsKey(slot)) {
//						slotTime = slotActualMap.get(slot).getOperationsStartAsDateTime();
//					}
					final Port nextPort = slot.getPort();
					if (lastPort != null && lastTime != null) {
						final int minTravelTime = portModel == null ? 0 : getTravelTime(assignableElement, portModel, nextPort, lastPort);

						// latest Departure time
						ZonedDateTime nextTime = lastTime.minusHours(minTravelTime);

						int duration = slot.getDuration();
//						if (slotActualMap.containsKey(slot)) {
//							SlotActuals slotActuals = slotActualMap.get(slot);
//							duration = Hours.between(slotActuals.getOperationsStartAsDateTime(), slotActuals.getOperationsEndAsDateTime());
//						}

						nextTime = nextTime.minusHours(duration);

						if (nextTime.isAfter(slotTime)) {
							lastTime = slotTime;
						} else {
							lastTime = nextTime;
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

			this.startWindow = new Pair<>(firstSlot.getWindowStartWithSlotOrPortTime(), firstSlot.getWindowEndWithSlotOrPortTime());
			this.endWindow = new Pair<>(lastSlot.getWindowStartWithSlotOrPortTime().plusHours(lastSlot.getDuration()), lastSlot.getWindowEndWithSlotOrPortTime().plusHours(lastSlot.getDuration()));

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
				assert false;
				startWindow.setFirst(maxStartDate);
			}

			final int ladenDuration = Hours.between(startWindow.getSecond(), endWindow.getFirst());
			this.minElementDuration = ladenDuration + lastSlot.getDuration();

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
				final RouteDistanceLineCache cache = (RouteDistanceLineCache) Platform.getAdapterManager().loadAdapter(route, RouteDistanceLineCache.class.getName());
				if (cache != null) {
					final int distance = cache.getDistance(from, to);

					if (distance == Integer.MAX_VALUE) {
						continue;
					}

					final double travelTime = distance / maxSpeed;
					int totalTime = (int) (Math.floor(travelTime));

					for (final VesselClassRouteParameters p : vesselClass.getRouteParameters()) {
						if (p.getRoute() == route) {
							totalTime += p.getExtraTransitTime();
						}
					}

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

}
