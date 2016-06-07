/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.util.scheduling.WrappedAssignableElement;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.lng.types.util.SetUtils;

/**
 */
public class AssignmentEditorHelper {
	public static Pair<ZonedDateTime, ZonedDateTime> getStartPeriodIgnoreSpots(@Nullable final AssignableElement element) {
		if (element instanceof Cargo) {
			final Cargo cargo = (Cargo) element;
			final List<Slot> sortedSlots = cargo.getSortedSlots();
			if (sortedSlots != null) {
				for (final Slot slot : sortedSlots) {
					if (slot instanceof SpotSlot) {
						continue;
					}
					return new Pair<>(slot.getWindowStartWithSlotOrPortTime(), slot.getWindowEndWithSlotOrPortTime());
				}
			}
		}
		return getStartPeriod(element);
	}

	public static ZonedDateTime getStartDateIgnoreSpots(@Nullable final AssignableElement element) {
		if (element instanceof Cargo) {
			final Cargo cargo = (Cargo) element;
			final List<Slot> sortedSlots = cargo.getSortedSlots();
			if (sortedSlots != null) {
				for (final Slot slot : sortedSlots) {
					if (slot instanceof SpotSlot) {
						continue;
					}
					return slot.getWindowStartWithSlotOrPortTime();
				}
			}
		}
		return getStartDate(element);
	}

	public static @Nullable Pair<ZonedDateTime, ZonedDateTime> getStartPeriod(@Nullable final AssignableElement element) {

		if (element instanceof Cargo) {
			final Cargo cargo = (Cargo) element;
			final EList<Slot> slots = cargo.getSortedSlots();
			if (slots.isEmpty()) {
				return null;
			}
			final Slot firstSlot = slots.get(0);
			return new Pair<>(firstSlot.getWindowStartWithSlotOrPortTime(), firstSlot.getWindowEndWithSlotOrPortTime());
		} else if (element instanceof VesselEvent) {
			final VesselEvent vesselEvent = (VesselEvent) element;

			return new Pair<>(vesselEvent.getStartAfterAsDateTime(), vesselEvent.getStartByAsDateTime());
		} else if (element instanceof Slot) {
			final Slot slot = (Slot) element;
			return new Pair<>(slot.getWindowStartWithSlotOrPortTime(), slot.getWindowEndWithSlotOrPortTime());
		} else {
			return null;
		}
	}

	public static @Nullable ZonedDateTime getStartDate(@Nullable final AssignableElement element) {

		if (element instanceof Cargo) {
			final Cargo cargo = (Cargo) element;
			final EList<Slot> slots = cargo.getSortedSlots();
			if (slots.isEmpty()) {
				return null;
			}
			final Slot firstSlot = slots.get(0);
			return firstSlot.getWindowStartWithSlotOrPortTime();
		} else if (element instanceof VesselEvent) {
			return ((VesselEvent) element).getStartByAsDateTime();
		} else if (element instanceof Slot) {
			return ((Slot) element).getWindowStartWithSlotOrPortTime();
		} else {
			return null;
		}
	}

	@Nullable
	public static ZonedDateTime getEndDateIgnoreSpots(@Nullable final AssignableElement element) {

		if (element instanceof Cargo) {
			final Cargo cargo = (Cargo) element;
			final List<Slot> sortedSlots = cargo.getSortedSlots();
			if (sortedSlots != null) {
				for (final Slot slot : sortedSlots) {
					if (slot instanceof SpotSlot) {
						continue;
					}
					return slot.getWindowEndWithSlotOrPortTime();
				}
			}
		}
		return getEndDate(element);
	}

	@Nullable
	public static ZonedDateTime getEndDate(@Nullable final AssignableElement element) {
		if (element instanceof Cargo) {
			final Cargo cargo = (Cargo) element;
			final EList<Slot> slots = cargo.getSortedSlots();
			if (slots.isEmpty()) {
				return null;
			}
			final Slot lastSlot = slots.get(slots.size() - 1);
			if (lastSlot.getWindowStart() != null) {
				return lastSlot.getWindowEndWithSlotOrPortTime();
			}
		} else if (element instanceof VesselEvent) {
			final ZonedDateTime dateTime = ((VesselEvent) element).getStartByAsDateTime();
			if (dateTime != null) {
				return dateTime.plusDays(((VesselEvent) element).getDurationInDays());
			}
		} else if (element instanceof Slot) {
			final Slot slot = (Slot) element;
			if (slot.getWindowStart() != null) {
				return slot.getWindowEndWithSlotOrPortTime();
			}
		}
		return null;
	}

	public static List<@NonNull CollectedAssignment> collectAssignments(@NonNull final CargoModel cargoModel, @NonNull final PortModel portModel, @NonNull final SpotMarketsModel spotMarketsModel) {
		return collectAssignments(cargoModel, portModel, spotMarketsModel, new AssignableElementDateComparator());
	}

	public static List<@NonNull CollectedAssignment> collectAssignments(@NonNull final CargoModel cargoModel, @NonNull final PortModel portModel, @NonNull final SpotMarketsModel spotMarketsModel,
			@NonNull final IAssignableElementComparator assignableElementComparator) {

		// Map the vessel availability to assignents
		final Map<VesselAvailability, List<AssignableElement>> fleetGrouping = new HashMap<>();
		// Keep the same order as the EMF data model
		final List<VesselAvailability> vesselAvailabilityOrder = new ArrayList<>();
		for (final VesselAvailability va : cargoModel.getVesselAvailabilities()) {
			vesselAvailabilityOrder.add(va);
			// Pre-create map values
			fleetGrouping.put(va, new ArrayList<>());
		}

		// Spot markets are keyed by market and instance/spot index
		final List<Pair<CharterInMarket, Integer>> charterInMarketKeysOrder = new ArrayList<>();
		final Map<Pair<CharterInMarket, Integer>, List<AssignableElement>> spotGrouping = new HashMap<>();
		for (final CharterInMarket charterInMarket : spotMarketsModel.getCharterInMarkets()) {
			for (int i = -1; i < charterInMarket.getSpotCharterCount(); ++i) {
				final Pair<CharterInMarket, Integer> key = new Pair<CharterInMarket, Integer>(charterInMarket, i);
				charterInMarketKeysOrder.add(key);
				// Pre-create map values
				spotGrouping.put(key, new ArrayList<AssignableElement>());
			}
		}

		// Loop over all assignable things - shipped cargoes and vessel events and allocate them their vessel assignment.
		final Set<AssignableElement> assignableElements = new LinkedHashSet<>();
		assignableElements.addAll(cargoModel.getCargoes());
		assignableElements.addAll(cargoModel.getVesselEvents());
		for (final AssignableElement assignableElement : assignableElements) {
			final VesselAssignmentType vesselAssignmentType = assignableElement.getVesselAssignmentType();
			if (vesselAssignmentType == null) {
				continue;
			}

			if (vesselAssignmentType instanceof CharterInMarket) {
				final CharterInMarket charterInMarket = (CharterInMarket) vesselAssignmentType;
				// Use vessel index normally, but for spots include spot index
				final Pair<CharterInMarket, Integer> key = new Pair<>(charterInMarket, assignableElement.getSpotIndex());
				// Groupings should have been pre-created
				spotGrouping.get(key).add(assignableElement);

			} else if (vesselAssignmentType instanceof VesselAvailability) {
				final VesselAvailability vesselAvailability = (VesselAvailability) vesselAssignmentType;
				// Groupings should have been pre-created
				fleetGrouping.get(vesselAvailability).add(assignableElement);
			}
		}

		// Final sorted list of assignment, ordered by fleet then spot
		final List<@NonNull CollectedAssignment> result = new ArrayList<>();

		// First add in the fleet vessels
		for (final VesselAvailability vesselAvailability : vesselAvailabilityOrder) {
			result.add(new CollectedAssignment(fleetGrouping.get(vesselAvailability), vesselAvailability, (PortModel) null));//, assignableElementComparator));
		}

		// Now add in the spot charter-ins
		for (final Pair<CharterInMarket, Integer> key : charterInMarketKeysOrder) {
			result.add(new CollectedAssignment(spotGrouping.get(key), key.getFirst(), key.getSecond(), portModel));
		}

		return result;
	}

	public static VesselAvailability findVesselAvailability(@NonNull final Vessel vessel, @NonNull final AssignableElement assignableElement,
			@NonNull final List<VesselAvailability> vesselAvailabilities) {

		int mightMatchCount = 0;
		for (final VesselAvailability vesselAvailability : vesselAvailabilities) {
			if (vesselAvailability.getVessel() == vessel) {
				mightMatchCount++;
				if (isElementInVesselAvailability(assignableElement, vesselAvailability)) {
					return vesselAvailability;
				}
			}
		}
		// Passed through first loop with out finding a vessel availability covering the assigned element. However if we did find a single availability matching the vessel, return that. If multiple,
		// give up.
		if (mightMatchCount == 1) {
			for (final VesselAvailability vesselAvailability : vesselAvailabilities) {
				if (vesselAvailability.getVessel() == vessel) {
					return vesselAvailability;
				}
			}
		}

		return null;
	}

	private static boolean isElementInVesselAvailability(@NonNull final AssignableElement element, @NonNull final VesselAvailability vesselAvailability) {

		final ZonedDateTime vesselAvailabilityEndBy = vesselAvailability.getEndByAsDateTime();
		final ZonedDateTime vesselAvailabilityStartAfter = vesselAvailability.getStartAfterAsDateTime();
		if (element instanceof Cargo) {
			final Cargo cargo = (Cargo) element;
			final List<Slot> sortedSlots = cargo.getSortedSlots();
			final Slot firstSlot = sortedSlots.get(0);
			final Slot lastSlot = sortedSlots.get(sortedSlots.size() - 1);

			if (firstSlot.getWindowStart() == null || lastSlot.getWindowStart() == null) {
				return false;
			}

			if (vesselAvailabilityEndBy != null) {
				if (firstSlot.getWindowStartWithSlotOrPortTime().isAfter(vesselAvailabilityEndBy)) {
					return false;
				}
			}
			if (vesselAvailability.getStartAfter() != null) {
				if (lastSlot.getWindowEndWithSlotOrPortTime().isAfter(vesselAvailabilityStartAfter)) {
					return false;
				}
			}
		} else if (element instanceof VesselEvent) {
			final VesselEvent event = (VesselEvent) element;
			if (vesselAvailabilityEndBy != null) {
				final ZonedDateTime eventStartAfter = event.getStartAfterAsDateTime();
				if (eventStartAfter.isAfter(vesselAvailabilityEndBy)) {
					return false;
				}
			}
			if (vesselAvailabilityStartAfter != null) {
				ZonedDateTime eventStartBy = event.getStartByAsDateTime();
				eventStartBy = eventStartBy.plusDays(event.getDurationInDays());
				if (eventStartBy.isBefore(vesselAvailabilityStartAfter)) {
					return false;
				}
			}
		}

		return true;
	}

	public static int getMaxSpot(@NonNull final CargoModel cargoModel) {
		int maxSpot = 0;
		for (final Cargo cargo : cargoModel.getCargoes()) {
			final VesselAssignmentType vesselAssignmentType = cargo.getVesselAssignmentType();
			if (vesselAssignmentType instanceof CharterInMarket) {
				maxSpot = Math.max(maxSpot, cargo.getSpotIndex());
			}
		}
		for (final VesselEvent vesselEvent : cargoModel.getVesselEvents()) {
			final VesselAssignmentType vesselAssignmentType = vesselEvent.getVesselAssignmentType();
			if (vesselAssignmentType instanceof CharterInMarket) {
				maxSpot = Math.max(maxSpot, vesselEvent.getSpotIndex());
			}
		}
		maxSpot++;
		return maxSpot;
	}

	public static boolean compileAllowedVessels(@NonNull final List<AVesselSet<Vessel>> allowedVessels, @NonNull EObject target) {
		// The slot intersection may mean no vessels are permitted at all!
		boolean noVesselsAllowed = false;
		// populate the list of allowed vessels for the target object

		if (target instanceof Slot) {
			final Slot slot = (Slot) target;
			final Cargo cargo = slot.getCargo();
			if (cargo != null) {
				// Change target and fall through.
				target = cargo;
			} else {
				final List<AVesselSet<Vessel>> slotVessels = slot.getAllowedVessels();
				if (slotVessels == null || slotVessels.isEmpty()) {
					return true;
				}
				allowedVessels.addAll(slotVessels);
				return false;
			}
		}

		if (target instanceof Cargo) {
			final Cargo cargo = (Cargo) target;
			final Set<AVesselSet<Vessel>> vessels = new LinkedHashSet<>();
			boolean first = true;
			for (final Slot s : cargo.getSlots()) {
				final List<AVesselSet<Vessel>> slotVessels = s.getAllowedVessels();
				if (slotVessels == null || slotVessels.isEmpty()) {
					continue;
				}
				if (first) {
					vessels.addAll(slotVessels);
					first = false;
				} else {
					// TODO: hmm, should we check classes vs vessels here?
					final Set<AVesselSet<Vessel>> matchedByClass = new LinkedHashSet<>();
					//
					for (final AVesselSet<Vessel> v1 : vessels) {
						if (v1 instanceof Vessel) {
							for (final AVesselSet<Vessel> v2 : slotVessels) {
								if (SetUtils.getObjects(v2).contains(v1)) {
									matchedByClass.add(v1);
								}
							}
						}
					}
					// Reverse map
					for (final AVesselSet<Vessel> v1 : slotVessels) {
						if (v1 instanceof Vessel) {
							for (final AVesselSet<Vessel> v2 : vessels) {
								if (SetUtils.getObjects(v2).contains(v1)) {
									matchedByClass.add(v1);
								}
							}
						}
					}

					// Exact matches
					vessels.retainAll(slotVessels);
					// Add in VesselClass/Group hits
					vessels.addAll(matchedByClass);
				}
			}
			allowedVessels.addAll(vessels);
			if (vessels.isEmpty() && first == false) {
				// Uh oh - set intersection resulted in nothing!
				noVesselsAllowed = true;
			}
		} else if (target instanceof VesselEvent) {
			final VesselEvent event = (VesselEvent) target;
			allowedVessels.addAll(event.getAllowedVessels());
		} else {
			// allowedVessels = null;
		}
		return noVesselsAllowed;
	}

	public enum OrderingHint {
		BEFORE, AFTER, AMBIGUOUS
	}

	public static @NonNull OrderingHint checkOrdering(@NonNull final AssignableElement a, @NonNull final AssignableElement b) {
		if (a instanceof Cargo && b instanceof Cargo) {
			return checkCargoOrdering((Cargo) a, (Cargo) b);
		} else if (a instanceof VesselEvent && b instanceof VesselEvent) {
			return checkEventOrdering((VesselEvent) a, (VesselEvent) b);
		} else if (a instanceof Cargo && b instanceof VesselEvent) {
			return checkCargoToEventOrdering((Cargo) a, (VesselEvent) b);
		} else if (b instanceof Cargo && a instanceof VesselEvent) {
			// Use same method, but flip return ordering
			final OrderingHint h = checkCargoToEventOrdering((Cargo) b, (VesselEvent) a);
			if (h == OrderingHint.BEFORE) {
				return OrderingHint.AFTER;
			} else if (h == OrderingHint.AFTER) {
				return OrderingHint.BEFORE;
			} else {
				return h;
			}
		} else {
			throw new IllegalArgumentException();
		}
	}

	public static @NonNull OrderingHint checkOrdering(@NonNull final WrappedAssignableElement a, @NonNull final WrappedAssignableElement b) {
		// Simple check on load events.
		boolean isBefore = false;
		boolean isAfter = false;

		if (!a.getEndWindow().getFirst().isAfter(b.getStartWindow().getSecond())) {
			isBefore = true;
		}
		if (!b.getEndWindow().getFirst().isAfter(a.getStartWindow().getSecond())) {
			isAfter = true;
		}

		if (isBefore == isAfter) {
			return OrderingHint.AMBIGUOUS;
		} else if (isBefore) {
			return OrderingHint.BEFORE;
		} else {
			assert isAfter;
			return OrderingHint.AFTER;
		}
	}

	private static @NonNull OrderingHint checkCargoOrdering(@NonNull final Cargo a, @NonNull final Cargo b) {
		final Slot a_load = a.getSortedSlots().get(0);
		final Slot b_load = b.getSortedSlots().get(0);

		final Slot a_discharge = a.getSortedSlots().get(a.getSlots().size() - 1);
		final Slot b_discharge = b.getSortedSlots().get(a.getSlots().size() - 1);

		// Simple check on load events.
		if (a_load.getWindowEndWithSlotOrPortTime().isBefore(b_load.getWindowStartWithSlotOrPortTime())) {
			return OrderingHint.BEFORE;
		}
		if (b_load.getWindowEndWithSlotOrPortTime().isBefore(a_load.getWindowStartWithSlotOrPortTime())) {
			return OrderingHint.AFTER;
		}
		// Loads Overlap, so check discharges
		if (a_discharge.getWindowEndWithSlotOrPortTime().isBefore(b_discharge.getWindowStartWithSlotOrPortTime())) {
			return OrderingHint.BEFORE;
		}
		if (b_discharge.getWindowEndWithSlotOrPortTime().isBefore(a_discharge.getWindowStartWithSlotOrPortTime())) {
			return OrderingHint.AFTER;
		}

		// Both loads and discharges overlap, can one cargo sort before the other still?

		// Load a could go after discharge b
		if (a_load.getWindowEndWithSlotOrPortTime().isAfter(b_discharge.getWindowStartWithSlotOrPortTime())) {
			return OrderingHint.AFTER;
		}
		if (b_load.getWindowEndWithSlotOrPortTime().isAfter(a_discharge.getWindowStartWithSlotOrPortTime())) {
			return OrderingHint.BEFORE;
		}

		// End of discharge event is before other cargoes load window end
		if (getMinEndDate(a).isAfter(b_load.getWindowEndWithSlotOrPortTime()) && !(getMinEndDate(b).isAfter(a_load.getWindowEndWithSlotOrPortTime()))) {
			return OrderingHint.BEFORE;
		}
		if (getMinEndDate(b).isAfter(a_load.getWindowEndWithSlotOrPortTime()) && !(getMinEndDate(a).isAfter(b_load.getWindowEndWithSlotOrPortTime()))) {
			return OrderingHint.AFTER;
		}

		return OrderingHint.AMBIGUOUS;

	}

	private static @NonNull OrderingHint checkCargoToEventOrdering(@NonNull final Cargo a, @NonNull final VesselEvent b) {
		final Slot a_load = a.getSortedSlots().get(0);
		final Slot a_discharge = a.getSortedSlots().get(a.getSlots().size() - 1);

		// Simple check on windows
		if (a_discharge.getWindowEndWithSlotOrPortTime().isBefore(b.getStartAfterAsDateTime())) {
			return OrderingHint.BEFORE;
		}
		if (b.getStartByAsDateTime().isBefore(a_load.getWindowStartWithSlotOrPortTime())) {
			return OrderingHint.AFTER;
		}

		// Overlapping, but only on one side
		if (a_load.getWindowEndWithSlotOrPortTime().isBefore(b.getStartAfterAsDateTime()) && a_discharge.getWindowEndWithSlotOrPortTime().isBefore(b.getStartByAsDateTime())) {
			return OrderingHint.BEFORE;
		}
		if (b.getStartByAsDateTime().isBefore(a_discharge.getWindowEndWithSlotOrPortTime()) && b.getStartAfterAsDateTime().isBefore(a_load.getWindowEndWithSlotOrPortTime())) {
			return OrderingHint.AFTER;
		}

		// Overlapping both sides, but event duration blocks out one side.
		if (b.getStartByAsDateTime().isAfter(a_discharge.getWindowEndWithSlotOrPortTime()) //
				&& getMinEndDate(b).isAfter(a_load.getWindowEndWithSlotOrPortTime())) {
			return OrderingHint.BEFORE;
		}

		return OrderingHint.AMBIGUOUS;
	}

	private static @NonNull OrderingHint checkEventOrdering(@NonNull final VesselEvent a, @NonNull final VesselEvent b) {

		// Simple check on windows
		if (a.getStartByAsDateTime().isBefore(b.getStartAfterAsDateTime())) {
			return OrderingHint.BEFORE;
		}
		if (b.getStartByAsDateTime().isBefore(a.getStartAfterAsDateTime())) {
			return OrderingHint.AFTER;
		}

		// Overlapping, but only on one side
		if (getMinEndDate(a).isAfter(b.getStartByAsDateTime())) {
			return OrderingHint.AFTER;
		}
		if (getMinEndDate(b).isAfter(a.getStartByAsDateTime())) {
			return OrderingHint.BEFORE;
		}

		return OrderingHint.AMBIGUOUS;
	}

	public static @NonNull ZonedDateTime getMinEndDate(@NonNull final AssignableElement e) {
		if (e instanceof Cargo) {
			return getMinEndDate(((Cargo) e));
		} else if (e instanceof VesselEvent) {
			return getMinEndDate((VesselEvent) e);
		}
		throw new IllegalArgumentException();
	}

	public static @NonNull ZonedDateTime getMinEndDate(@NonNull final VesselEvent e) {
		return e.getStartAfterAsDateTime().plusDays(e.getDurationInDays());
	}

	public static @NonNull ZonedDateTime getMinEndDate(@NonNull final Cargo c) {
		ZonedDateTime z = null;
		for (final Slot s : c.getSortedSlots()) {
			final ZonedDateTime earliestFinish = s.getWindowStartWithSlotOrPortTime().plusHours(s.getSlotOrPortDuration());
			if (z == null) {
				z = earliestFinish;
			} else {
				if (earliestFinish.isAfter(z)) {
					z = earliestFinish;
				}
			}

		}
		if (z == null) {
			throw new IllegalStateException();
		}

		return z;
	}

	public static boolean checkInsertion(@NonNull final AssignableElement before, @NonNull final AssignableElement e, @NonNull final AssignableElement after) {

		final OrderingHint beforeHint = checkOrdering(before, e);
		final OrderingHint afterHint = checkOrdering(after, e);
		// Check for simple cases first
		if (beforeHint == OrderingHint.BEFORE && afterHint == OrderingHint.AFTER) {
			return true;
		}
		if (beforeHint == OrderingHint.BEFORE && afterHint == OrderingHint.BEFORE) {
			return false;
		}
		if (beforeHint == OrderingHint.AFTER && afterHint == OrderingHint.AFTER) {
			return false;
		}
		// What? - really an error?
		if (beforeHint == OrderingHint.AFTER && afterHint == OrderingHint.BEFORE) {
			return false;
		}

		// If we get here, one or both hints will be AMBIGUOUS
		@NonNull
		final ZonedDateTime beforeEarliestEnd = getMinEndDate(before);

		@NonNull
		final ZonedDateTime afterLatestStart = getStartPeriodIgnoreSpots(after).getSecond();

		// Can we insert the element between windows (ignoring any transit times)
		return Hours.between(beforeEarliestEnd, afterLatestStart) > getDurationInHours(e);

	}

	public static boolean checkInsertion(@NonNull final WrappedAssignableElement before, @NonNull final WrappedAssignableElement e, @NonNull final WrappedAssignableElement after) {

		final OrderingHint beforeHint = checkOrdering(before, e);
		final OrderingHint afterHint = checkOrdering(after, e);
		// Check for simple cases first
		if (beforeHint == OrderingHint.BEFORE && afterHint == OrderingHint.AFTER) {
			return true;
		}
		if (beforeHint == OrderingHint.BEFORE && afterHint == OrderingHint.BEFORE) {
			return false;
		}
		if (beforeHint == OrderingHint.AFTER && afterHint == OrderingHint.AFTER) {
			return false;
		}
		// What? - really an error?
		if (beforeHint == OrderingHint.AFTER && afterHint == OrderingHint.BEFORE) {
			return false;
		}

		// If we get here, one or both hints will be AMBIGUOUS
		@NonNull
		final ZonedDateTime beforeEarliestEnd = before.getEndWindow().getFirst();

		@NonNull
		final ZonedDateTime afterLatestStart = after.getStartWindow().getSecond();

		// Can we insert the element between windows (ignoring any transit times)
		return Hours.between(beforeEarliestEnd, afterLatestStart) > e.getMinEventDurationInHours();

	}

	private static int getDurationInHours(@NonNull final AssignableElement e) {
		if (e instanceof Cargo) {
			return getDurationInHours(((Cargo) e));
		} else if (e instanceof VesselEvent) {
			return getDurationInHours((VesselEvent) e);
		}
		throw new IllegalArgumentException();
	}

	private static int getDurationInHours(@NonNull final Cargo c) {
		ZonedDateTime start = null;
		ZonedDateTime z = null;
		for (final Slot s : c.getSortedSlots()) {
			final ZonedDateTime windowStartWithSlotOrPortTime = s.getWindowStartWithSlotOrPortTime();
			if (start == null) {
				start = windowStartWithSlotOrPortTime;
			}
			final ZonedDateTime earliestFinish = windowStartWithSlotOrPortTime.plusHours(s.getSlotOrPortDuration());
			if (z == null) {
				z = earliestFinish;
			} else {
				if (earliestFinish.isAfter(z)) {
					z = earliestFinish;
				}
			}

		}
		if (z == null) {
			throw new IllegalStateException();
		}
		if (start == null) {
			throw new IllegalStateException();
		}

		return Hours.between(start, z);
	}

	private static int getDurationInHours(@NonNull final VesselEvent e) {

		return 24 * e.getDurationInDays();
	}
}
