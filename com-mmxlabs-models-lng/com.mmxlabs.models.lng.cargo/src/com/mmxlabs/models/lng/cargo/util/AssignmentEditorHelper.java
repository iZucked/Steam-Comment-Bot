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
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.lng.types.util.SetUtils;

/**
 */
public class AssignmentEditorHelper {
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

	public static ZonedDateTime getStartDate(@Nullable final AssignableElement element) {

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
					return slot.getWindowStartWithSlotOrPortTime();
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

	public static List<@NonNull CollectedAssignment> collectAssignments(@NonNull final CargoModel cargoModel, @NonNull final SpotMarketsModel spotMarketsModel) {
		return collectAssignments(cargoModel, spotMarketsModel, new AssignableElementDateComparator());
	}

	public static List<@NonNull CollectedAssignment> collectAssignments(@NonNull final CargoModel cargoModel, @NonNull final SpotMarketsModel spotMarketsModel,
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
			result.add(new CollectedAssignment(fleetGrouping.get(vesselAvailability), vesselAvailability, assignableElementComparator));
		}

		// Now add in the spot charter-ins
		for (final Pair<CharterInMarket, Integer> key : charterInMarketKeysOrder) {
			result.add(new CollectedAssignment(spotGrouping.get(key), key.getFirst(), key.getSecond()));
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
}
