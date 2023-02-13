/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.util.scheduling.WrappedAssignableElement;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.lng.types.util.SetUtils;

/**
 */
public class AssignmentEditorHelper {
	public static Pair<ZonedDateTime, ZonedDateTime> getStartPeriodIgnoreSpots(@Nullable final AssignableElement element) {
		if (element instanceof Cargo cargo) {
			final List<Slot<?>> sortedSlots = cargo.getSortedSlots();
			if (sortedSlots != null) {
				for (final Slot<?> slot : sortedSlots) {
					if (slot instanceof SpotSlot) {
						continue;
					}
					return new Pair<>(slot.getSchedulingTimeWindow().getStart(), slot.getSchedulingTimeWindow().getEnd());
				}
			}
		}
		return getStartPeriod(element);
	}

	public static ZonedDateTime getStartDateIgnoreSpots(@Nullable final AssignableElement element) {
		if (element instanceof Cargo cargo) {
			final List<Slot<?>> sortedSlots = cargo.getSortedSlots();
			if (sortedSlots != null) {
				for (final Slot<?> slot : sortedSlots) {
					if (slot instanceof SpotSlot) {
						continue;
					}
					return slot.getSchedulingTimeWindow().getStart();
				}
			}
		}
		return getStartDate(element);
	}

	public static @Nullable Pair<ZonedDateTime, ZonedDateTime> getStartPeriod(@Nullable final AssignableElement element) {

		if (element instanceof Cargo cargo) {
			final List<Slot<?>> slots = cargo.getSortedSlots();
			if (slots.isEmpty()) {
				return null;
			}
			final Slot<?> firstSlot = slots.get(0);
			return new Pair<>(firstSlot.getSchedulingTimeWindow().getStart(), firstSlot.getSchedulingTimeWindow().getEnd());
		} else if (element instanceof VesselEvent vesselEvent) {

			return new Pair<>(vesselEvent.getStartAfterAsDateTime(), vesselEvent.getStartByAsDateTime());
		} else if (element instanceof Slot<?> slot) {
			return new Pair<>(slot.getSchedulingTimeWindow().getStart(), slot.getSchedulingTimeWindow().getEnd());
		} else {
			return null;
		}
	}

	public static @Nullable ZonedDateTime getStartDate(@Nullable final AssignableElement element) {

		if (element instanceof Cargo cargo) {
			final List<Slot<?>> slots = cargo.getSortedSlots();
			if (slots.isEmpty()) {
				return null;
			}
			final Slot<?> firstSlot = slots.get(0);
			return firstSlot.getSchedulingTimeWindow().getStart();
		} else if (element instanceof VesselEvent vesselEvent) {
			return vesselEvent.getStartByAsDateTime();
		} else if (element instanceof Slot<?> slot) {
			return slot.getSchedulingTimeWindow().getStart();
		} else {
			return null;
		}
	}

	@Nullable
	public static ZonedDateTime getEndDateIgnoreSpots(@Nullable final AssignableElement element) {

		if (element instanceof Cargo cargo) {
			final List<Slot<?>> sortedSlots = cargo.getSortedSlots();
			if (sortedSlots != null) {
				for (final Slot<?> slot : sortedSlots) {
					if (slot instanceof SpotSlot) {
						continue;
					}
					return slot.getSchedulingTimeWindow().getEnd();
				}
			}
		}
		return getEndDate(element);
	}

	@Nullable
	public static ZonedDateTime getEndDate(@Nullable final AssignableElement element) {
		if (element instanceof Cargo cargo) {
			final List<Slot<?>> slots = cargo.getSortedSlots();
			if (slots.isEmpty()) {
				return null;
			}
			final Slot<?> lastSlot = slots.get(slots.size() - 1);
			if (lastSlot.getWindowStart() != null) {
				return lastSlot.getSchedulingTimeWindow().getEnd();
			}
		} else if (element instanceof VesselEvent) {
			final ZonedDateTime dateTime = ((VesselEvent) element).getStartByAsDateTime();
			if (dateTime != null) {
				return dateTime.plusDays(((VesselEvent) element).getDurationInDays());
			}
		} else if (element instanceof Slot) {
			final Slot<?> slot = (Slot<?>) element;
			if (slot.getWindowStart() != null) {
				return slot.getSchedulingTimeWindow().getEnd();
			}
		}
		return null;
	}

	public static @Nullable List<@NonNull CollectedAssignment> collectAssignments(@NonNull final CargoModel cargoModel, @NonNull final PortModel portModel,
			@NonNull final SpotMarketsModel spotMarketsModel, @NonNull ModelDistanceProvider modelDistanceProvider) {
		return collectAssignments(cargoModel, portModel, spotMarketsModel, modelDistanceProvider, null, null);
	}

	public static @Nullable List<@NonNull CollectedAssignment> collectAssignments(@NonNull final CargoModel cargoModel, @NonNull final PortModel portModel,
			@NonNull final SpotMarketsModel spotMarketsModel, @NonNull ModelDistanceProvider modelDistanceProvider, @Nullable final IAssignableElementDateProvider assignableElementDateProvider,
			@Nullable IExtraDataProvider extraDataProvider) {

		// Map the vessel availability to assignments
		final Map<VesselCharter, List<AssignableElement>> fleetGrouping = new HashMap<>();
		final Map<CharterInMarketOverride, List<AssignableElement>> marketOverridesGrouping = new HashMap<>();
		// Keep the same order as the EMF data model
		final List<VesselCharter> vesselCharterOrder = new ArrayList<>();
		List<VesselCharter> combinedCharters = new LinkedList<>();
		combinedCharters.addAll(cargoModel.getVesselCharters());
		if (extraDataProvider != null) {
			combinedCharters.addAll(extraDataProvider.getExtraVesselCharters());
		}
		for (final VesselCharter va : combinedCharters) {
			vesselCharterOrder.add(va);
			// Pre-create map values
			fleetGrouping.put(va, new ArrayList<>());
		}

		// Spot markets are keyed by market and instance/spot index
		final List<Pair<CharterInMarket, Integer>> charterInMarketKeysOrder = new ArrayList<>();
		final Map<Pair<CharterInMarket, Integer>, List<AssignableElement>> spotGrouping = new HashMap<>();

		List<CharterInMarket> combinedCharterInMarkets = new LinkedList<>();
		combinedCharterInMarkets.addAll(spotMarketsModel.getCharterInMarkets());
		if (extraDataProvider != null) {
			combinedCharterInMarkets.addAll(extraDataProvider.getExtraCharterInMarkets());
		}

		for (final CharterInMarket charterInMarket : combinedCharterInMarkets) {
			int start = charterInMarket.isNominal() ? -1 : 0;
			for (int i = start; i < charterInMarket.getSpotCharterCount(); ++i) {
				final Pair<CharterInMarket, Integer> key = new Pair<>(charterInMarket, i);
				charterInMarketKeysOrder.add(key);
				// Pre-create map values
				spotGrouping.put(key, new ArrayList<>());
			}
		}

		// Loop over all assignable things - shipped cargoes and vessel events and allocate them their vessel assignment.
		final Set<AssignableElement> assignableElements = new LinkedHashSet<>();
		assignableElements.addAll(cargoModel.getCargoes());
		assignableElements.addAll(cargoModel.getVesselEvents());
		if (extraDataProvider != null) {
			assignableElements.addAll(extraDataProvider.getExtraVesselEvents());
		}
		for (final AssignableElement assignableElement : assignableElements) {

			if (assignableElement instanceof VesselEvent vesselEvent) {
				if (vesselEvent.getStartBy() == null || vesselEvent.getStartAfter() == null) {
					// No date, cannot continue.
					return null;
				}

			} else if (assignableElement instanceof Cargo cargo) {
				if (cargo.getSlots().isEmpty()) {
					return null;
				}
				for (final Slot slot : cargo.getSlots()) {
					if (slot.getWindowStart() == null) {
						// No date, cannot continue.
						return null;
					}
				}
			}

			final VesselAssignmentType vesselAssignmentType = assignableElement.getVesselAssignmentType();
			if (vesselAssignmentType == null) {
				continue;
			}

			if (vesselAssignmentType instanceof CharterInMarket charterInMarket) {
				// Use vessel index normally, but for spots include spot index
				final Pair<CharterInMarket, Integer> key = new Pair<>(charterInMarket, assignableElement.getSpotIndex());

				if (!spotGrouping.containsKey(key)) {
					charterInMarketKeysOrder.add(key);
					spotGrouping.put(key, new ArrayList<>());
				}

				// Groupings should have been pre-created
				spotGrouping.get(key).add(assignableElement);

			} else if (vesselAssignmentType instanceof VesselCharter vesselCharter) {
				// Groupings should have been pre-created
				List<AssignableElement> list = fleetGrouping.get(vesselCharter);
				if (list == null) {
					// Unexpected state, cannot continue (can happen if a vesselCharter has been deleted)
					return null;
				}
				list.add(assignableElement);
			} else if (vesselAssignmentType instanceof CharterInMarketOverride charterInMarketOverride) {
				marketOverridesGrouping.computeIfAbsent(charterInMarketOverride, k -> new LinkedList<>()).add(assignableElement);
			}
		}

		// Final sorted list of assignment, ordered by fleet then spot
		final List<@NonNull CollectedAssignment> result = new ArrayList<>();

		// First add in the fleet vessels
		for (final VesselCharter vesselCharter : vesselCharterOrder) {
			result.add(new CollectedAssignment(fleetGrouping.get(vesselCharter), vesselCharter, portModel, modelDistanceProvider, assignableElementDateProvider));
		}

		// Now add in the spot charter-ins
		for (final Pair<CharterInMarket, Integer> key : charterInMarketKeysOrder) {
			result.add(new CollectedAssignment(spotGrouping.get(key), key.getFirst(), key.getSecond(), portModel, modelDistanceProvider, assignableElementDateProvider));
		}

		return result;
	}

	public static VesselCharter findVesselCharter(@NonNull final Vessel vessel, @NonNull final List<VesselCharter> vesselCharters, @Nullable final Integer charterIndex) {

		final int idx = charterIndex == null ? 1 : charterIndex.intValue();
		for (final VesselCharter vesselCharter : vesselCharters) {
			if (vesselCharter.getVessel() == vessel && vesselCharter.getCharterNumber() == idx) {
				return vesselCharter;
			}
		}

		return null;
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

	public static List<Vessel> compileAllowedVessels(List<Vessel> availableVessels, @NonNull EObject target) {

		// ALL WRONG!
		//
		// Needs input list of options,
		//
		// Needs to habndle permiossive etr
		List<Vessel> validVessels = new LinkedList<>(availableVessels);
		// The slot intersection may mean no vessels are permitted at all!
		boolean noVesselsAllowed = false;
		// populate the list of allowed vessels for the target object

		if (target instanceof Slot<?> slot) {
			final Cargo cargo = slot.getCargo();
			if (cargo != null) {
				// Change target and fall through.
				target = cargo;
			} else {
				final Set<Vessel> slotVessels = SetUtils.getObjects(slot.getSlotOrDelegateVesselRestrictions());
				if (slot.getSlotOrDelegateVesselRestrictionsArePermissive()) {
					validVessels.retainAll(slotVessels);
				} else {
					validVessels.removeAll(slotVessels);
				}
				return validVessels;
			}
		}

		if (target instanceof Cargo cargo) {
			final Set<AVesselSet<Vessel>> vessels = new LinkedHashSet<>();
			boolean first = true;
			for (final Slot<?> slot : cargo.getSlots()) {

				final Set<Vessel> slotVessels = SetUtils.getObjects(slot.getSlotOrDelegateVesselRestrictions());
				if (slot.getSlotOrDelegateVesselRestrictionsArePermissive()) {
					validVessels.retainAll(slotVessels);
				} else {
					validVessels.removeAll(slotVessels);
				}
			}
			return validVessels;
		} else if (target instanceof VesselEvent event) {
			final Set<Vessel> eventVessels = SetUtils.getObjects(event.getAllowedVessels());
			if (!eventVessels.isEmpty()) {
				validVessels.retainAll(event.getAllowedVessels());
			}
			return validVessels;
		}
		return validVessels;
	}

	public enum OrderingHint {
		BEFORE, AFTER, AMBIGUOUS
	}

	public static @NonNull OrderingHint checkOrdering(@NonNull final WrappedAssignableElement a, @NonNull final WrappedAssignableElement b) {
		// Simple check on load events.
		boolean isBefore = false;
		boolean isAfter = false;

		// if a earliest end <= b latest start
		if (!a.getEndWindow().getFirst().isAfter(b.getStartWindow().getSecond())) {
			isBefore = true;
		}
		// if b earliest end <= a latest start
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

	public static @NonNull ZonedDateTime getMinEndDate(@NonNull final VesselEvent e) {
		return e.getStartAfterAsDateTime().plusDays(e.getDurationInDays());
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
}
