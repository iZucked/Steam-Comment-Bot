/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.CharterLengthEvent;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;

/**
 * TODO: {@link #getSegmentStart(Event)} and {@link #getSegmentEnd(Event)}
 * should be replaceable with the new {@link EventGrouping} interface.
 * 
 * @author sg
 *
 */
public class ScheduleModelUtils {

	/**
	 * Returns true if the event is the start of a sequence of events (and thus the
	 * prior events sequence ends) For example this could the Load up to the end of
	 * the voyage before another load.
	 * 
	 * @param event
	 * @return
	 */
	public static boolean isSegmentStart(final Event event) {
		if (event instanceof VesselEventVisit) {
			return true;
		} else if (event instanceof GeneratedCharterOut) {
			return true;
		} else if (event instanceof CharterLengthEvent) {
			return true;
		} else if (event instanceof StartEvent) {
			return true;
		} else if (event instanceof EndEvent) {
			return true;
		} else if (event instanceof SlotVisit) {
			final SlotAllocation slotAllocation = ((SlotVisit) event).getSlotAllocation();
			if (slotAllocation != null) {
				if (slotAllocation.getSlot() instanceof LoadSlot) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * For the given event, find the segment start.
	 * 
	 * @param event
	 * @return
	 */
	public static Event getSegmentStart(final Event event) {

		Event start = event;
		// Find segment start
		while (start != null && !isSegmentStart(start)) {
			start = start.getPreviousEvent();
		}
		return start;
	}

	/**
	 * For the given event, find the last segment event.
	 * 
	 * @param event
	 * @return
	 */
	public static Event getSegmentEnd(final Event event) {

		Event prevEvent = event;
		Event currentEvent = event.getNextEvent();
		// Find segment start
		while (currentEvent != null && !isSegmentStart(currentEvent)) {
			prevEvent = currentEvent;
			currentEvent = currentEvent.getNextEvent();
		}
		return prevEvent;
	}

	/**
	 * Returns the total duration from start of this event to the start of the next
	 * segment.
	 * 
	 * See {@link ScheduleModelUtils#isSegmentStart(Event)}
	 * 
	 * @param event
	 * @return
	 */
	public static int getEventDuration(final Event event) {

		int duration = event.getDuration();
		Event next = event.getNextEvent();
		while (next != null && !isSegmentStart(next)) {
			duration += next.getDuration();
			next = next.getNextEvent();
		}
		return duration;
	}

	/**
	 * Returns the main {@link Event} linked to the input. This could be a
	 * {@link LoadSlot} {@link SlotVisit}, or {@link VesselEventVisit} etc.
	 * 
	 * @param scheduleModelObject
	 */
	public static @Nullable PortVisit getMainEvent(EObject scheduleModelObject) {
		if (scheduleModelObject instanceof CargoAllocation) {
			final CargoAllocation cargoAllocation = (CargoAllocation) scheduleModelObject;
			scheduleModelObject = cargoAllocation.getSlotAllocations().get(0);
		}
		if (scheduleModelObject instanceof SlotAllocation) {
			final SlotAllocation slotAllocation = (SlotAllocation) scheduleModelObject;
			return slotAllocation.getSlotVisit();
		} else if (scheduleModelObject instanceof VesselEventVisit) {
			final VesselEventVisit vesselEventVisit = (VesselEventVisit) scheduleModelObject;
			return vesselEventVisit;
		} else if (scheduleModelObject instanceof GeneratedCharterOut) {
			final GeneratedCharterOut generatedCharterOut = (GeneratedCharterOut) scheduleModelObject;
			return generatedCharterOut;
		} else if (scheduleModelObject instanceof CharterLengthEvent) {
			final CharterLengthEvent charterLength = (CharterLengthEvent) scheduleModelObject;
			return charterLength;
		} else if (scheduleModelObject instanceof EndEvent) {
			final EndEvent endEvent = (EndEvent) scheduleModelObject;
			return endEvent;
		} else if (scheduleModelObject instanceof StartEvent) {
			final StartEvent startEvent = (StartEvent) scheduleModelObject;
			return startEvent;
		}
		return null;
	}

	public static @Nullable Journey getLinkedJourneyEvent(final PortVisit portVisit) {
		final Event evt = portVisit.getNextEvent();
		if (evt instanceof Journey) {
			return (Journey) evt;
		}
		return null;

	}

	public static @Nullable Idle getLinkedIdleEvent(final PortVisit portVisit) {
		Event evt = portVisit.getNextEvent();
		if (evt instanceof Journey) {
			evt = evt.getNextEvent();
		}
		if (evt instanceof Idle) {
			return (Idle) evt;
		}
		return null;

	}

	public static int sumFuelVolumes(final List<FuelQuantity> fuels, final FuelUnit fuelUnit) {
		int fuelTotal = 0;
		for (final FuelQuantity fuel : fuels) {
			for (final FuelAmount fuelAmount : fuel.getAmounts()) {
				if (fuelAmount.getUnit() == fuelUnit) {
					fuelTotal += fuelAmount.getQuantity();
				}
			}
		}
		return fuelTotal;
	}

	public static @Nullable Vessel getVessel(final @Nullable Sequence seq) {
		if (seq == null)
			return null;
		final VesselAvailability vesselAvailability = seq.getVesselAvailability();
		if (vesselAvailability != null) {
			return vesselAvailability.getVessel();
		}
		CharterInMarket charterInMarket = seq.getCharterInMarket();
		if (charterInMarket != null) {
			return charterInMarket.getVessel();
		}
		final CharterInMarketOverride charterInMarketOverride = seq.getCharterInMarketOverride();
		if (charterInMarketOverride != null) {
			charterInMarket = charterInMarketOverride.getCharterInMarket();
			if (charterInMarket != null) {
				return charterInMarket.getVessel();
			}
		}
		return null;
	}

	/**
	 * Match on same slots (although not, we are not checking order)
	 * 
	 * @param cargo
	 * @param cargoAllocation
	 * @return
	 */
	public static boolean matchingSlots(@Nullable final Cargo cargo, @Nullable final CargoAllocation cargoAllocation) {
		if (cargo == null || cargoAllocation == null) {
			return false;
		}
		if (cargo.getSlots().size() == cargoAllocation.getSlotAllocations().size()) {

			final Set<String> cargoIDs = new HashSet<>();
			final Set<String> allocationIDs = new HashSet<>();
			cargo.getSlots().forEach(s -> cargoIDs.add(s.getName()));
			cargoAllocation.getSlotAllocations().forEach(s -> allocationIDs.add(s.getName()));
			return cargoIDs.equals(allocationIDs);
		}
		return false;
	}

	public static @Nullable ProfitAndLossContainer getProfitAndLossContainer(final Object object) {
		ProfitAndLossContainer container = null;

		if (object instanceof CargoAllocation //
				|| object instanceof VesselEventVisit //
				|| object instanceof StartEvent //
				|| object instanceof GeneratedCharterOut //
				|| object instanceof CharterLengthEvent //
				|| object instanceof OpenSlotAllocation //
				|| object instanceof EndEvent) {
			container = (ProfitAndLossContainer) object;
		} else if (object instanceof SlotVisit) {
			final SlotVisit slotVisit = (SlotVisit) object;
			if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
				container = slotVisit.getSlotAllocation().getCargoAllocation();
			}
		}
		return container;
	}

	public static SlotAllocation getDischargeAllocation(final @NonNull CargoAllocation cargoAllocation) {
		for (final SlotAllocation sa : cargoAllocation.getSlotAllocations()) {
			if (sa.getSlot() instanceof DischargeSlot) {
				return sa;
			}
		}
		return null;
	}

	public static SlotAllocation getLoadAllocation(final @NonNull CargoAllocation cargoAllocation) {
		for (final SlotAllocation sa : cargoAllocation.getSlotAllocations()) {
			if (sa.getSlot() instanceof LoadSlot) {
				return sa;
			}
		}
		return null;
	}

	public static LoadSlot getLoadSlot(final Object allocation) {
		LoadSlot slot = null;
		if (allocation instanceof CargoAllocation) {
			final CargoAllocation ca = (CargoAllocation) allocation;
			for (final SlotAllocation sa : ca.getSlotAllocations()) {
				if (sa != null) {
					if (sa.getSlot() instanceof LoadSlot) {
						slot = (LoadSlot) sa.getSlot();
					}
				}
			}
		} else if (allocation instanceof SlotAllocation) {
			final SlotAllocation sa = (SlotAllocation) allocation;
			if (sa.getSlot() instanceof LoadSlot) {
				slot = (LoadSlot) sa.getSlot();
			}
		} else if (allocation instanceof OpenSlotAllocation) {
			final OpenSlotAllocation sa = (OpenSlotAllocation) allocation;
			if (sa.getSlot() instanceof LoadSlot) {
				slot = (LoadSlot) sa.getSlot();
			}
		}
		return slot;
	}

	public static DischargeSlot getDischargeSlot(final Object allocation) {
		DischargeSlot slot = null;
		if (allocation instanceof CargoAllocation) {
			final CargoAllocation ca = (CargoAllocation) allocation;
			for (final SlotAllocation sa : ca.getSlotAllocations()) {
				if (sa != null) {
					if (sa.getSlot() instanceof DischargeSlot) {
						slot = (DischargeSlot) sa.getSlot();
					}
				}
			}
		} else if (allocation instanceof SlotAllocation) {
			final SlotAllocation sa = (SlotAllocation) allocation;
			if (sa.getSlot() instanceof DischargeSlot) {
				slot = (DischargeSlot) sa.getSlot();
			}
		} else if (allocation instanceof OpenSlotAllocation) {
			final OpenSlotAllocation sa = (OpenSlotAllocation) allocation;
			if (sa.getSlot() instanceof DischargeSlot) {
				slot = (DischargeSlot) sa.getSlot();
			}
		}
		return slot;
	}

	public static String getLoadPortName(final Object allocation) {
		return getPortNameFromSlot(getLoadSlot(allocation));
	}

	public static String getLoadPortFullName(final Object allocation) {
		return getPortFullNameFromSlot(getLoadSlot(allocation));
	}

	public static String getDischargePortName(final Object allocation) {
		return getPortNameFromSlot(getDischargeSlot(allocation));
	}

	public static String getDischargePortFullName(final Object allocation) {
		return getPortFullNameFromSlot(getDischargeSlot(allocation));
	}

	private static String getPortNameFromSlot(final Slot<?> slot) {
		if (slot != null) {
			if (slot.getPort() != null) {
				final Port port = slot.getPort();
				if (port.getShortName() != null && !port.getShortName().isEmpty()) {
					return port.getShortName();
				}
				return port.getName();
			}
		}
		return "";
	}

	private static String getPortFullNameFromSlot(final Slot<?> slot) {
		if (slot != null) {
			if (slot.getPort() != null) {
				final Port port = slot.getPort();
				return port.getName();
			}
		}
		return "";
	}

	public static Port getLoadPort(final Object allocation) {
		return getPortFromSlot(getLoadSlot(allocation));
	}

	public static Port getDischargePort(final Object allocation) {
		return getPortFromSlot(getDischargeSlot(allocation));
	}

	private static Port getPortFromSlot(final Slot<?> slot) {
		if (slot != null) {
			return slot.getPort() != null ? slot.getPort() : null;
		}
		return null;
	}

	public static Vessel getVesselFromAllocation(final Object allocation) {
		if (allocation instanceof CargoAllocation) {
			final CargoAllocation cargoAllocation = (CargoAllocation) allocation;
			return getVessel(cargoAllocation.getSequence());
		}
		return null;
	}

}
