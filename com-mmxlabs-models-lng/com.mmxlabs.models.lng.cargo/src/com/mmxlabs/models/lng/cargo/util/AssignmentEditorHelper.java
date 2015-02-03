/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.management.timer.Timer;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.Slot;
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
	public static Date getStartDate(final AssignableElement task) {

		if (task instanceof Cargo) {
			final Cargo cargo = (Cargo) task;
			final EList<Slot> slots = cargo.getSortedSlots();
			if (slots.isEmpty()) {
				return null;
			}
			final Slot firstSlot = slots.get(0);
			return firstSlot.getWindowStartWithSlotOrPortTime();
		} else if (task instanceof VesselEvent) {
			return ((VesselEvent) task).getStartBy();
		} else if (task instanceof Slot) {
			return ((Slot) task).getWindowStartWithSlotOrPortTime();
		} else {
			return null;
		}
	}

	public static Date getEndDate(final AssignableElement task) {
		if (task instanceof Cargo) {
			final Cargo cargo = (Cargo) task;
			final EList<Slot> slots = cargo.getSortedSlots();
			if (slots.isEmpty()) {
				return null;
			}
			final Slot lastSlot = slots.get(slots.size() - 1);
			return lastSlot.getWindowEndWithSlotOrPortTime();
		} else if (task instanceof VesselEvent) {
			return new Date(((VesselEvent) task).getStartBy().getTime() + Timer.ONE_DAY * ((VesselEvent) task).getDurationInDays());
		} else if (task instanceof Slot) {
			return ((Slot) task).getWindowEndWithSlotOrPortTime();
		} else {
			return null;
		}
	}

	public static List<CollectedAssignment> collectAssignments(final CargoModel cargoModel, final SpotMarketsModel spotMarketsModel) {
		return collectAssignments(cargoModel, spotMarketsModel, new AssignableElementDateComparator());
	}

	public static List<CollectedAssignment> collectAssignments(final CargoModel cargoModel, final SpotMarketsModel spotMarketsModel, final IAssignableElementComparator assignableElementComparator) {
		final List<CollectedAssignment> result = new ArrayList<CollectedAssignment>();
		// Enforce consistent order
		final Map<Pair<VesselAvailability, Integer>, List<AssignableElement>> fleetGrouping = new TreeMap<Pair<VesselAvailability, Integer>, List<AssignableElement>>(
				new Comparator<Pair<VesselAvailability, Integer>>() {

					@Override
					public int compare(final Pair<VesselAvailability, Integer> o1, final Pair<VesselAvailability, Integer> o2) {

						final int c = o1.getSecond() - o2.getSecond();
						if (c == 0) {
							// Hmm, this could be bad, will we loose elements in the TreeMap?
							final int ii = 0; // Set a breakpoint!
						}

						return c;
					}
				});
		final Map<Triple<CharterInMarket, Integer, Integer>, List<AssignableElement>> spotGrouping = new TreeMap<>(new Comparator<Triple<CharterInMarket, Integer, Integer>>() {

			@Override
			public int compare(final Triple<CharterInMarket, Integer, Integer> o1, final Triple<CharterInMarket, Integer, Integer> o2) {

				int c = o1.getSecond() - o2.getSecond();
				if (c == 0) {
					if (o1.getThird() == o2.getThird()) {
						c = 0;
					} else if (o1.getThird() == null) {
						c = -1;
					} else if (o2.getThird() == null) {
						return 1;
					} else {
						c = o1.getThird() - o2.getThird();
					}
				}

				if (c == 0) {
					// Hmm, this could be bad, will we loose elements in the TreeMap?
					final int ii = 0; // Set a breakpoint!
				}

				return c;
			}
		});

		int index = 0;
		final List<VesselAvailability> vesselAvailabilityOrder = new ArrayList<>();
		for (final VesselAvailability va : cargoModel.getVesselAvailabilities()) {
			vesselAvailabilityOrder.add(va);
			fleetGrouping.put(new Pair<VesselAvailability, Integer>(va, index++), new ArrayList<AssignableElement>());
		}
		final List<CharterInMarket> charterInMarketOrder = new ArrayList<>();
		for (final CharterInMarket charterInMarket : spotMarketsModel.getCharterInMarkets()) {
			charterInMarketOrder.add(charterInMarket);
			spotGrouping.put(new Triple<CharterInMarket, Integer, Integer>(charterInMarket, index++, 0), new ArrayList<AssignableElement>());
		}

		final Set<AssignableElement> assignableElements = new LinkedHashSet<>();
		assignableElements.addAll(cargoModel.getCargoes());
		// assignableElements.addAll(cargoModel.getLoadSlots());
		// assignableElements.addAll(cargoModel.getDischargeSlots());
		assignableElements.addAll(cargoModel.getVesselEvents());
		for (final AssignableElement assignableElement : assignableElements) {
			final VesselAssignmentType vesselAssignmentType = assignableElement.getVesselAssignmentType();
			if (vesselAssignmentType == null) {
				continue;
			}

			if (vesselAssignmentType instanceof CharterInMarket) {
				final CharterInMarket charterInMarket = (CharterInMarket) vesselAssignmentType;
				// Use vessel index normally, but for spots include spot index
				final Triple<CharterInMarket, Integer, Integer> key = new Triple<>(charterInMarket, charterInMarketOrder.indexOf(charterInMarket), assignableElement.getSpotIndex());
				List<AssignableElement> l = spotGrouping.get(key);
				if (l == null) {
					l = new ArrayList<AssignableElement>();
					spotGrouping.put(key, l);
				}
				l.add(assignableElement);

			} else if (vesselAssignmentType instanceof VesselAvailability) {
				final VesselAvailability vesselAvailability = (VesselAvailability) vesselAssignmentType;

				// Use vessel index normally, but for spots include spot index
				final Pair<VesselAvailability, Integer> key = new Pair<>(vesselAvailability, vesselAvailabilityOrder.indexOf(vesselAvailability));
				List<AssignableElement> l = fleetGrouping.get(key);
				if (l == null) {
					l = new ArrayList<AssignableElement>();
					fleetGrouping.put(key, l);
				}
				l.add(assignableElement);
			}
		}

		for (final Pair<VesselAvailability, Integer> k : fleetGrouping.keySet()) {
			result.add(new CollectedAssignment(fleetGrouping.get(k), k.getFirst(), assignableElementComparator));
		}
		for (final Triple<CharterInMarket, Integer, Integer> k : spotGrouping.keySet()) {
			result.add(new CollectedAssignment(spotGrouping.get(k), k.getFirst(), k.getThird()));
		}

		return result;
	}

	public static VesselAvailability findVesselAvailability(final Vessel vessel, final AssignableElement assignableElement, final List<VesselAvailability> vesselAvailabilities) {

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

	private static boolean isElementInVesselAvailability(final AssignableElement element, final VesselAvailability vesselAvailability) {

		if (element instanceof Cargo) {
			final Cargo cargo = (Cargo) element;
			final List<Slot> sortedSlots = cargo.getSortedSlots();
			final Slot firstSlot = sortedSlots.get(0);
			final Slot lastSlot = sortedSlots.get(sortedSlots.size() - 1);

			if (vesselAvailability.getEndBy() != null) {
				if (firstSlot.getWindowStartWithSlotOrPortTime().after(vesselAvailability.getEndBy())) {
					return false;
				}
			}
			if (vesselAvailability.getStartAfter() != null) {
				if (lastSlot.getWindowEndWithSlotOrPortTime().before(vesselAvailability.getStartAfter())) {
					return false;
				}
			}
		} else if (element instanceof VesselEvent) {
			final VesselEvent event = (VesselEvent) element;
			if (vesselAvailability.getEndBy() != null) {
				if (event.getStartAfter().after(vesselAvailability.getEndBy())) {
					return false;
				}
			}
			if (vesselAvailability.getStartAfter() != null) {
				final Calendar cal = Calendar.getInstance();
				cal.setTime(event.getStartBy());
				cal.add(Calendar.DATE, event.getDurationInDays());
				if (cal.getTime().before(vesselAvailability.getStartAfter())) {
					return false;
				}
			}
		}

		return true;
	}

	public static int getMaxSpot(final CargoModel cargoModel) {
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

	public static boolean compileAllowedVessels(List<AVesselSet<Vessel>> allowedVessels, EObject target) {
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
			allowedVessels = null;
		}
		return noVesselsAllowed;
	}
}
