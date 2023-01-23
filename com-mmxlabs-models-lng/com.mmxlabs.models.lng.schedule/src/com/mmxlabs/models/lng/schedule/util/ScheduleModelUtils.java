/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.Sets;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.CharterLengthEvent;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Schedule;
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
 * @author sg & fm
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
		final VesselCharter vesselCharter = seq.getVesselCharter();
		if (vesselCharter != null) {
			return vesselCharter.getVessel();
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
	
	public static Cargo getCargoFromCargoAllocation(final @NonNull CargoAllocation cargoAllocation) {
		for (final SlotAllocation sa : cargoAllocation.getSlotAllocations()) {
			if (sa.getSlot() != null) {
				if (sa.getSlot().getCargo() != null) {
					return sa.getSlot().getCargo();
				}
			}
		}
		throw new IllegalStateException(String.format("%s: cargo allocation misses the cargo", ScheduleModelUtils.class.getCanonicalName()));
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

	public static @Nullable DischargeSlot getDischargeSlot(final Object allocation) {
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

	public static DischargeSlot getDischargeSlotFromCargo(final Cargo cargo) {
		DischargeSlot slot = null;
		if (cargo != null) {
			for (final Slot<?> s : cargo.getSlots()) {
				if (s instanceof DischargeSlot) {
					slot = (DischargeSlot) s;
				}
			}
		}
		return slot;
	}

	public static LoadSlot getLoadSlotFromCargo(final Cargo cargo) {
		LoadSlot slot = null;
		if (cargo != null) {
			for (final Slot<?> s : cargo.getSlots()) {
				if (s instanceof LoadSlot) {
					slot = (LoadSlot) s;
				}
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

	public static Port getPortFromSlot(final Slot<?> slot) {
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

	public static String getVesselNameFromAllocation(final Object allocation) {
		if (allocation instanceof CargoAllocation) {
			final CargoAllocation cargoAllocation = (CargoAllocation) allocation;
			final Vessel v = getVessel(cargoAllocation.getSequence());
			if (v != null) {
				return v.getName();
			}
		}
		return "";
	}

	private static int getFuelCost(final FuelUsage fuelUser, final Fuel... fuels) {
		final Set<Fuel> fuelsOfInterest = Sets.newHashSet(fuels);
		int sum = 0;
		if (fuelUser != null) {
			final EList<FuelQuantity> fuelQuantities = fuelUser.getFuels();
			for (final FuelQuantity fq : fuelQuantities) {
				if (fuelsOfInterest.contains(fq.getFuel())) {
					sum += fq.getCost();
				}
			}
		}
		return sum;
	}

	/**
	 * Returns the journey legs' duration in days (rounded to the next full integer)
	 * 
	 * @param cargoAllocation
	 * @return 0-Laden; 1-Ballast; 2-Laden(Idle); 3-Ballast(Idle); 4-Discharge;
	 *         5-Loading; 6-Total
	 * 
	 */
	public static int[] getJourneyDays(final CargoAllocation cargoAllocation, final boolean includeCharterOutInBallastIdle) {
		final int[] result = new int[7];
		if (cargoAllocation != null) {
			Event lastEvent = null;
			for (final Event e : cargoAllocation.getEvents()) {
				lastEvent = e;
				if (e instanceof Journey) {
					if (((Journey) e).isLaden()) {
						result[0] = e.getDuration();
					} else {
						result[1] = e.getDuration();
					}
				} else if (e instanceof Idle) {
					if (((Idle) e).isLaden()) {
						result[2] = e.getDuration();
					} else {
						result[3] = e.getDuration();
					}
				} else if (e instanceof SlotVisit) {
					final SlotVisit s = (SlotVisit) e;
					final SlotAllocation sa = s.getSlotAllocation();
					if (sa.getSlot() instanceof DischargeSlot) {
						result[4] = e.getDuration();
					} else if (sa.getSlot() instanceof LoadSlot) {
						result[5] = e.getDuration();
					}
				}
			}
			if (lastEvent != null && includeCharterOutInBallastIdle) {
				final Event ne = lastEvent.getNextEvent();
				if (ne instanceof CharterLengthEvent) {
					result[3] += ne.getDuration();
				}
			}
		}
		for (int i = 0; i < 6; i++) {
			result[6] += result[i];
		}
		for (int i = 0; i < 7; i++) {
			result[i] = (int) Math.ceil(result[i] / 24.0);
		}
		return result;
	}

	/**
	 * Returns the journey legs' duration in days (real values)
	 * 
	 * @param cargoAllocation
	 * @return 0-Laden; 1-Ballast; 2-Laden(Idle); 3-Ballast(Idle); 4-Discharge;
	 *         5-Loading; 6-Total
	 * 
	 */
	public static double[] getRawJourneyDays(final CargoAllocation cargoAllocation, final boolean includeCharterOutInBallastIdle) {
		final double[] result = new double[7];
		if (cargoAllocation != null) {
			Event lastEvent = null;
			for (final Event e : cargoAllocation.getEvents()) {
				lastEvent = e;
				if (e instanceof Journey) {
					if (((Journey) e).isLaden()) {
						result[0] = e.getDuration();
					} else {
						result[1] = e.getDuration();
					}
				} else if (e instanceof Idle) {
					if (((Idle) e).isLaden()) {
						result[2] = e.getDuration();
					} else {
						result[3] = e.getDuration();
					}
				} else if (e instanceof SlotVisit) {
					final SlotVisit s = (SlotVisit) e;
					final SlotAllocation sa = s.getSlotAllocation();
					if (sa.getSlot() instanceof DischargeSlot) {
						result[4] = e.getDuration();
					} else if (sa.getSlot() instanceof LoadSlot) {
						result[5] = e.getDuration();
					}
				}
			}
			if (lastEvent != null && includeCharterOutInBallastIdle) {
				final Event ne = lastEvent.getNextEvent();
				if (ne instanceof CharterLengthEvent) {
					result[3] += ne.getDuration();
				}
			}
		}
		for (int i = 0; i < 6; i++) {
			result[6] += result[i];
		}
		for (int i = 0; i < 7; i++) {
			result[i] = result[i] / 24.0;
		}
		return result;
	}

	public static StartEvent getStartEvent(final VesselCharter vesselCharter, final Schedule schedule) {
		final Sequence sequence = schedule.getSequences().stream().filter(s -> s.getVesselCharter().equals(vesselCharter)).findFirst().get();
		final Event event = sequence.getEvents().get(0);
		assert event instanceof StartEvent;
		return (StartEvent) event;
	}

	public static EndEvent getEndEvent(final VesselCharter vesselCharter, final Schedule schedule) {
		final Sequence sequence = schedule.getSequences().stream().filter(s -> s.getVesselCharter().equals(vesselCharter)).findFirst().get();
		final Event event = sequence.getEvents().get(sequence.getEvents().size() - 1);
		assert event instanceof EndEvent;
		return (EndEvent) event;
	}

	public static StartEvent getStartEvent(final CharterInMarket charterInMarket, final int spotIndex, final Schedule schedule) {
		final Sequence sequence = schedule.getSequences().stream() //
				.filter(s -> charterInMarket.equals(s.getCharterInMarket())) //
				.filter(s -> s.getSpotIndex() == spotIndex) //
				.findFirst().get();
		final Event event = sequence.getEvents().get(0);
		assert event instanceof StartEvent;
		return (StartEvent) event;
	}

	public static EndEvent getEndEvent(final CharterInMarket charterInMarket, final int spotIndex, final Schedule schedule) {
		final Sequence sequence = schedule.getSequences().stream() //
				.filter(s -> charterInMarket.equals(s.getCharterInMarket())) //
				.filter(s -> s.getSpotIndex() == spotIndex) //
				.findFirst().get();
		final Event event = sequence.getEvents().get(sequence.getEvents().size() - 1);
		assert event instanceof EndEvent;
		return (EndEvent) event;
	}
}
