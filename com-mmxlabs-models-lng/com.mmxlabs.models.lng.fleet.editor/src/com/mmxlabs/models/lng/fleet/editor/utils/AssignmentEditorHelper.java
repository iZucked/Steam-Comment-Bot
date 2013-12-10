/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.editor.utils;

import java.util.ArrayList;
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
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.common.Triple;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.AssignableElement;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.ScenarioFleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselAvailability;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.types.AVesselSet;

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

	public static List<CollectedAssignment> collectAssignments(final CargoModel cargoModel, final FleetModel fleetModel, final ScenarioFleetModel scenarioFleetModel) {
		final List<CollectedAssignment> result = new ArrayList<CollectedAssignment>();
		// Enforce consistent order
		final Map<Triple<AVesselSet<? extends Vessel>, Integer, Integer>, List<AssignableElement>> grouping = new TreeMap<Triple<AVesselSet<? extends Vessel>, Integer, Integer>, List<AssignableElement>>(
				new Comparator<Triple<AVesselSet<? extends Vessel>, Integer, Integer>>() {

					@Override
					public int compare(final Triple<AVesselSet<? extends Vessel>, Integer, Integer> o1, final Triple<AVesselSet<? extends Vessel>, Integer, Integer> o2) {

						int c = o1.getSecond() - o2.getSecond();
						if (c == 0) {
							c = o1.getThird() - o2.getThird();
						}

						return c;
					}
				});

		int index = 0;
		final List<AVesselSet<Vessel>> vesselOrder = new ArrayList<AVesselSet<Vessel>>();
		for (final VesselAvailability va : scenarioFleetModel.getVesselAvailabilities()) {
			final Vessel v = va.getVessel();
			vesselOrder.add(v);
			grouping.put(new Triple<AVesselSet<? extends Vessel>, Integer, Integer>(v, index++, 0), new ArrayList<AssignableElement>());
		}
		for (final VesselClass vesselClass : fleetModel.getVesselClasses()) {
			vesselOrder.add(vesselClass);
			grouping.put(new Triple<AVesselSet<? extends Vessel>, Integer, Integer>(vesselClass, index++, 0), new ArrayList<AssignableElement>());
		}

		Set<AssignableElement> assignableElements = new LinkedHashSet<>();
		assignableElements.addAll(cargoModel.getCargoes());
		assignableElements.addAll(cargoModel.getLoadSlots());
		assignableElements.addAll(cargoModel.getDischargeSlots());
		assignableElements.addAll(scenarioFleetModel.getVesselEvents());
		for (final AssignableElement assignableElement : assignableElements) {
			if (assignableElement.getAssignment() == null) {
				continue;
			}

			// Use vessel index normally, but for spots include spot index
			final Triple<AVesselSet<? extends Vessel>, Integer, Integer> k = new Triple<AVesselSet<? extends Vessel>, Integer, Integer>(assignableElement.getAssignment(), vesselOrder.indexOf(assignableElement.getAssignment()), assignableElement.getSpotIndex());
			List<AssignableElement> l = grouping.get(k);
			if (l == null) {
				l = new ArrayList<AssignableElement>();
				grouping.put(k, l);
			}
			l.add(assignableElement);
		}

		for (final Triple<AVesselSet<? extends Vessel>, Integer, Integer> k : grouping.keySet()) {
			result.add(new CollectedAssignment(grouping.get(k), k.getFirst(), k.getThird()));
		}

		return result;
	}

	public static Command reassignElement(final EditingDomain ed, final AssignableElement beforeTask, final AssignableElement task, final AssignableElement afterTask,
			final AVesselSet<Vessel> vesselOrClass, final int spotIndex) {
		final CompoundCommand cc = new CompoundCommand();
		cc.append(SetCommand.create(ed, task, FleetPackage.Literals.ASSIGNABLE_ELEMENT__ASSIGNMENT, vesselOrClass == null ? SetCommand.UNSET_VALUE : vesselOrClass));
		cc.append(SetCommand.create(ed, task, FleetPackage.eINSTANCE.getAssignableElement_SpotIndex(), spotIndex));
		if (beforeTask != null) {
			final int newSeq = beforeTask.getSequenceHint() + 1;
			cc.append(SetCommand.create(ed, task, FleetPackage.eINSTANCE.getAssignableElement_SequenceHint(), newSeq));
		} else if (afterTask != null) {
			final int newSeq = afterTask.getSequenceHint() - 1;
			cc.append(SetCommand.create(ed, afterTask, FleetPackage.eINSTANCE.getAssignableElement_SequenceHint(), newSeq));
		}

		return cc;
	}

	public static Command reassignElement(final EditingDomain ed, final AVesselSet<Vessel> destination, final AssignableElement ea, final int maxSpot) {
		if (ea == null)
			return IdentityCommand.INSTANCE;

		final CompoundCommand cc = new CompoundCommand();

		cc.append(SetCommand.create(ed, ea, FleetPackage.eINSTANCE.getAssignableElement_Assignment(), destination == null ? SetCommand.UNSET_VALUE : destination));
		cc.append(SetCommand.create(ed, ea, FleetPackage.eINSTANCE.getAssignableElement_SpotIndex(), maxSpot));

		return cc;
	}

	public static int getMaxSpot(final CargoModel cargoModel, final ScenarioFleetModel scenarioFleetModel) {
		int maxSpot = 0;
		for (Cargo cargo : cargoModel.getCargoes()) {
			if (cargo.getAssignment() != null) {

				maxSpot = Math.max(maxSpot, cargo.getSpotIndex());
			}
		}
		for (LoadSlot loadSlot : cargoModel.getLoadSlots()) {
			if (loadSlot.getAssignment() != null) {

				maxSpot = Math.max(maxSpot, loadSlot.getSpotIndex());
			}
		}
		for (DischargeSlot dischargeSlot : cargoModel.getDischargeSlots()) {
			if (dischargeSlot.getAssignment() != null) {

				maxSpot = Math.max(maxSpot, dischargeSlot.getSpotIndex());
			}
		}
		for (VesselEvent vesselEvent : scenarioFleetModel.getVesselEvents()) {
			if (vesselEvent.getAssignment() != null) {
				maxSpot = Math.max(maxSpot, vesselEvent.getSpotIndex());
			}
		}
		maxSpot++;
		return maxSpot;
	}
}
