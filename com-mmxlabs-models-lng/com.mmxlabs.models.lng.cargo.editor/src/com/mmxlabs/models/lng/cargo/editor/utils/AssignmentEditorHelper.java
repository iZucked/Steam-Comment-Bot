/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.utils;

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

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.types.AVesselSet;
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

	public static List<CollectedAssignment> collectAssignments(final CargoModel cargoModel, final FleetModel fleetModel) {
		return collectAssignments(cargoModel, fleetModel, null);
	}

	public static List<CollectedAssignment> collectAssignments(final CargoModel cargoModel, final FleetModel fleetModel, IAssignableElementComparator assignableElementComparator) {
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
		final Map<Triple<VesselClass, Integer, Integer>, List<AssignableElement>> spotGrouping = new TreeMap<Triple<VesselClass, Integer, Integer>, List<AssignableElement>>(
				new Comparator<Triple<VesselClass, Integer, Integer>>() {

					@Override
					public int compare(final Triple<VesselClass, Integer, Integer> o1, final Triple<VesselClass, Integer, Integer> o2) {

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
		final List<VesselClass> vesselClassOrder = new ArrayList<>();
		for (final VesselAvailability va : cargoModel.getVesselAvailabilities()) {
			vesselAvailabilityOrder.add(va);
			fleetGrouping.put(new Pair<VesselAvailability, Integer>(va, index++), new ArrayList<AssignableElement>());
		}
		for (final VesselClass vesselClass : fleetModel.getVesselClasses()) {
			vesselClassOrder.add(vesselClass);
			spotGrouping.put(new Triple<VesselClass, Integer, Integer>(vesselClass, index++, 0), new ArrayList<AssignableElement>());
		}

		final Set<AssignableElement> assignableElements = new LinkedHashSet<>();
		assignableElements.addAll(cargoModel.getCargoes());
		assignableElements.addAll(cargoModel.getLoadSlots());
		assignableElements.addAll(cargoModel.getDischargeSlots());
		assignableElements.addAll(cargoModel.getVesselEvents());
		for (final AssignableElement assignableElement : assignableElements) {
			if (assignableElement.getAssignment() == null) {
				continue;
			}

			if (assignableElement.isSetSpotIndex()) {
				// Use vessel index normally, but for spots include spot index
				final Triple<VesselClass, Integer, Integer> key = new Triple<VesselClass, Integer, Integer>((VesselClass) assignableElement.getAssignment(), vesselClassOrder.indexOf(assignableElement
						.getAssignment()), assignableElement.getSpotIndex());
				List<AssignableElement> l = spotGrouping.get(key);
				if (l == null) {
					l = new ArrayList<AssignableElement>();
					spotGrouping.put(key, l);
				}
				l.add(assignableElement);

			} else {
				final Vessel vessel = (Vessel) assignableElement.getAssignment();
				final VesselAvailability vesselAvailability = findVesselAvailability(vessel, assignableElement, cargoModel.getVesselAvailabilities());

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
		for (final Triple<VesselClass, Integer, Integer> k : spotGrouping.keySet()) {
			result.add(new CollectedAssignment(spotGrouping.get(k), k.getFirst(), k.getThird()));
		}

		return result;
	}

	private static VesselAvailability findVesselAvailability(final Vessel vessel, final AssignableElement assignableElement, final List<VesselAvailability> vesselAvailabilities) {

		for (final VesselAvailability vesselAvailability : vesselAvailabilities) {
			if (vesselAvailability.getVessel() == vessel) {
				return vesselAvailability;
			}
		}

		return null;
	}

	private boolean isElementInVesselAvailability(final AssignableElement element, final VesselAvailability vesselAvailability) {

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

	public static Command reassignElement(final EditingDomain ed, final AssignableElement beforeTask, final AssignableElement task, final AssignableElement afterTask,
			final AVesselSet<Vessel> vesselOrClass, final int spotIndex) {
		final CompoundCommand cc = new CompoundCommand();
		cc.append(SetCommand.create(ed, task, CargoPackage.Literals.ASSIGNABLE_ELEMENT__ASSIGNMENT, vesselOrClass == null ? SetCommand.UNSET_VALUE : vesselOrClass));
		cc.append(SetCommand.create(ed, task, CargoPackage.eINSTANCE.getAssignableElement_SpotIndex(), spotIndex));
		if (beforeTask != null) {
			final int newSeq = beforeTask.getSequenceHint() + 1;
			cc.append(SetCommand.create(ed, task, CargoPackage.eINSTANCE.getAssignableElement_SequenceHint(), newSeq));
		} else if (afterTask != null) {
			final int newSeq = afterTask.getSequenceHint() - 1;
			cc.append(SetCommand.create(ed, afterTask, CargoPackage.eINSTANCE.getAssignableElement_SequenceHint(), newSeq));
		}

		return cc;
	}

	public static Command reassignElement(final EditingDomain ed, final AVesselSet<Vessel> destination, final AssignableElement ea, final int maxSpot) {
		if (ea == null)
			return IdentityCommand.INSTANCE;

		final CompoundCommand cc = new CompoundCommand();

		cc.append(SetCommand.create(ed, ea, CargoPackage.eINSTANCE.getAssignableElement_Assignment(), destination == null ? SetCommand.UNSET_VALUE : destination));
		cc.append(SetCommand.create(ed, ea, CargoPackage.eINSTANCE.getAssignableElement_SpotIndex(), maxSpot));

		return cc;
	}

	public static int getMaxSpot(final CargoModel cargoModel) {
		int maxSpot = 0;
		for (final Cargo cargo : cargoModel.getCargoes()) {
			if (cargo.getAssignment() != null) {
				if (cargo.isSetSpotIndex()) {
					maxSpot = Math.max(maxSpot, cargo.getSpotIndex());
				}
			}
		}
		for (final LoadSlot loadSlot : cargoModel.getLoadSlots()) {
			if (loadSlot.getAssignment() != null) {
				if (loadSlot.isSetSpotIndex()) {
					maxSpot = Math.max(maxSpot, loadSlot.getSpotIndex());
				}
			}
		}
		for (final DischargeSlot dischargeSlot : cargoModel.getDischargeSlots()) {
			if (dischargeSlot.getAssignment() != null) {
				if (dischargeSlot.isSetSpotIndex()) {
					maxSpot = Math.max(maxSpot, dischargeSlot.getSpotIndex());
				}
			}
		}
		for (final VesselEvent vesselEvent : cargoModel.getVesselEvents()) {
			if (vesselEvent.getAssignment() != null) {
				if (vesselEvent.isSetSpotIndex()) {
					maxSpot = Math.max(maxSpot, vesselEvent.getSpotIndex());
				}
			}
		}
		maxSpot++;
		return maxSpot;
	}

	public static boolean compileAllowedVessels(List<AVesselSet<Vessel>> allowedVessels, final EObject target) {
		// The slot intersection may mean no vessels are permitted at all!
		boolean noVesselsAllowed = false;
		// populate the list of allowed vessels for the target object
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
