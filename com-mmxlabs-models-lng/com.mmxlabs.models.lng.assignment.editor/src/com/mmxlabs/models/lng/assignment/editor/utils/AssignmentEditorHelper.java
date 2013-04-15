/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.editor.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.management.timer.Timer;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.common.Triple;
import com.mmxlabs.models.lng.assignment.AssignmentModel;
import com.mmxlabs.models.lng.assignment.AssignmentPackage;
import com.mmxlabs.models.lng.assignment.ElementAssignment;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.mmxcore.UUIDObject;

public class AssignmentEditorHelper {
	public static Date getStartDate(final UUIDObject task) {

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

	public static Date getEndDate(final UUIDObject task) {
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

	//
	// public static Command taskReassigned(final EditingDomain ed, final AssignmentModel modelObject, UUIDObject task, UUIDObject beforeTask,
	// UUIDObject afterTask, Assignment oldResource,
	// Assignment newResource) {
	// final CompoundCommand cc = new CompoundCommand();
	//
	// // this should definitely kill anything pre-existing
	// Command totallyUnassign = totallyUnassign(ed, modelObject, task);
	// cc.append(totallyUnassign);
	//
	// // copy the current state and remove all ocurrences of task, in order that when we get
	// // the position it's valid after the unassign command has executed.
	// final List<UUIDObject> assigned = new ArrayList<UUIDObject>(newResource.getAssignedObjects());
	// assigned.removeAll(Collections.singleton(task));
	//
	// int position;
	// if (beforeTask != null) {
	// position = assigned.indexOf(beforeTask);
	// } else if (afterTask != null) {
	// position = assigned.indexOf(afterTask) + 1;
	// } else {
	// position = 0;
	//
	// final Date start = getStartDate(task);
	// final Date end = getEndDate(task);
	//
	// if (start != null && end != null) {
	// for (final UUIDObject o : assigned) {
	// final Date startO = getStartDate(o);
	// // final Date endO = getEndDate(o);
	// if (end.before(startO)) {
	// break;
	// // } else if (start.after(endO)) {
	// // position++;
	// // break;
	// } else {
	// position++;
	// }
	// }
	// }
	// }
	//
	// if (newResource.getAssignedObjects().isEmpty() || position == newResource.getAssignedObjects().size()) {
	// cc.append(AddCommand.create(ed, newResource, AssignmentPackage.eINSTANCE.getAssignment_AssignedObjects(), task));
	// } else {
	// cc.append(AddCommand.create(ed, newResource, AssignmentPackage.eINSTANCE.getAssignment_AssignedObjects(), task, position));
	// }
	//
	// return cc;
	// }
	//
	// public static Command taskUnassigned(final EditingDomain ed, final AssignmentModel modelObject, UUIDObject task, Assignment oldResource) {
	// final CompoundCommand cc = new CompoundCommand();
	// cc.append(RemoveCommand.create(ed, oldResource, AssignmentPackage.eINSTANCE.getAssignment_AssignedObjects(), task));
	// cc.append(RemoveCommand.create(ed, modelObject, AssignmentPackage.eINSTANCE.getAssignmentModel_LockedAssignedObjects(), task));
	// return cc;
	// }
	//
	// public static Command totallyUnassign(final EditingDomain ed, final AssignmentModel modelObject, final UUIDObject task) {
	// final CompoundCommand kill = new CompoundCommand();
	// kill.append(IdentityCommand.INSTANCE);
	// for (final Assignment a : modelObject.getAssignments()) {
	// for (final UUIDObject b : a.getAssignedObjects()) {
	// if (b == task) {
	// kill.append(RemoveCommand.create(ed, a, AssignmentPackage.eINSTANCE.getAssignment_AssignedObjects(), task));
	// }
	// }
	// }
	// return kill;
	// }
	//
	// public static Assignment getAssignmentForTask(final AssignmentModel inputModel, final UUIDObject object) {
	// for (final Assignment a : inputModel.getAssignments()) {
	// if (a.getAssignedObjects().contains(object)) {
	// return a;
	// }
	// }
	// return null;
	// }
	//
	// public static Assignment getAssignmentForVessel(final AssignmentModel input, final AVesselSet set) {
	// for (final Assignment a : input.getAssignments()) {
	// if (a.getVessels().contains(set)) {
	// return a;
	// }
	// }
	// return null;
	// }

	public static ElementAssignment getElementAssignment(final AssignmentModel modelObject, final UUIDObject task) {
		// Need to clone and check for nulls to avoid concurrent modification issues.
		final List<ElementAssignment> elementAssignments = new ArrayList<ElementAssignment>(modelObject.getElementAssignments());
		for (final ElementAssignment ea : elementAssignments) {
			if (ea != null && ea.getAssignedObject() == task) {
				return ea;
			}
		}
		return null;
	}

	// ELEMENT ASSIGNMENT STUFF

	public static Command unassignElement(final EditingDomain ed, final AssignmentModel modelObject, final UUIDObject task) {
		final ElementAssignment ea = getElementAssignment(modelObject, task);
		return unassignElement(ed, ea);
	}

	public static Command unassignElement(final EditingDomain ed, final ElementAssignment ea) {
		if (ea == null)
			return IdentityCommand.INSTANCE;
		final CompoundCommand cc = new CompoundCommand();

		cc.append(SetCommand.create(ed, ea, AssignmentPackage.eINSTANCE.getElementAssignment_Assignment(), SetCommand.UNSET_VALUE));
		cc.append(SetCommand.create(ed, ea, AssignmentPackage.eINSTANCE.getElementAssignment_Locked(), false));

		return cc;
	}

	public static Command reassignElement(final EditingDomain ed, final AssignmentModel modelObject, final UUIDObject task, final AVesselSet<Vessel> destination) {
		final ElementAssignment ea = getElementAssignment(modelObject, task);
		return reassignElement(ed, destination, ea, destination instanceof VesselClass ? getMaxSpot(modelObject) : 0);
	}

	public static Command reassignElement(final EditingDomain ed, final AVesselSet<Vessel> destination, final ElementAssignment ea) {
		return reassignElement(ed, destination, ea, 0);
	}

	public static Command lockElement(final EditingDomain ed, final AssignmentModel modelObject, final UUIDObject task) {
		return lockElement(ed, getElementAssignment(modelObject, task));
	}

	public static Command unlockElement(final EditingDomain ed, final AssignmentModel modelObject, final UUIDObject task) {
		return unlockElement(ed, getElementAssignment(modelObject, task));
	}

	public static Command lockElement(final EditingDomain ed, final ElementAssignment ea) {
		if (ea == null) {
			return IdentityCommand.INSTANCE;
		}
		return SetCommand.create(ed, ea, AssignmentPackage.eINSTANCE.getElementAssignment_Locked(), true);
	}

	public static Command unlockElement(final EditingDomain ed, final ElementAssignment ea) {
		if (ea == null)
			return IdentityCommand.INSTANCE;
		return SetCommand.create(ed, ea, AssignmentPackage.eINSTANCE.getElementAssignment_Locked(), false);
	}

	public static List<CollectedAssignment> collectAssignments(final AssignmentModel im, final FleetModel fm) {
		final List<CollectedAssignment> result = new ArrayList<CollectedAssignment>();
		// Enforce consistent order
		final Map<Triple<AVesselSet<Vessel>, Integer, Integer>, List<ElementAssignment>> grouping = new TreeMap<Triple<AVesselSet<Vessel>, Integer, Integer>, List<ElementAssignment>>(
				new Comparator<Triple<AVesselSet<Vessel>, Integer, Integer>>() {

					@Override
					public int compare(final Triple<AVesselSet<Vessel>, Integer, Integer> o1, final Triple<AVesselSet<Vessel>, Integer, Integer> o2) {

						int c = o1.getSecond() - o2.getSecond();
						if (c == 0) {
							c = o1.getThird() - o2.getThird();
						}

						return c;
					}
				});

		int index = 0;
		final List<Vessel> vesselOrder = new ArrayList<Vessel>();
		for (final Vessel v : fm.getVessels()) {
			vesselOrder.add(v);
			grouping.put(new Triple<AVesselSet<Vessel>, Integer, Integer>(v, 0, index++), new ArrayList<ElementAssignment>());
		}

		for (final ElementAssignment ea : im.getElementAssignments()) {
			if (ea.getAssignment() == null) {
				continue;
			}
			// Use vessel index normally, but for spots include spot index
			final int third = vesselOrder.contains(ea.getAssignment()) ? vesselOrder.indexOf(ea.getAssignment()) : index + ea.getSpotIndex();
			final Triple<AVesselSet<Vessel>, Integer, Integer> k = new Triple<AVesselSet<Vessel>, Integer, Integer>(ea.getAssignment(), ea.getSpotIndex(), third);
			List<ElementAssignment> l = grouping.get(k);
			if (l == null) {
				l = new ArrayList<ElementAssignment>();
				grouping.put(k, l);
			}
			l.add(ea);
		}

		for (final Triple<AVesselSet<Vessel>, Integer, Integer> k : grouping.keySet()) {
			result.add(new CollectedAssignment(grouping.get(k), k.getFirst(), k.getSecond()));
		}

		return result;
	}

	public static Command reassignElement(final EditingDomain ed, final AssignmentModel modelObject, final UUIDObject beforeTask, final UUIDObject task, final UUIDObject afterTask,
			final AVesselSet<Vessel> vesselOrClass, final int spotIndex) {
		final ElementAssignment ea = getElementAssignment(modelObject, task);
		if (ea == null)
			return IdentityCommand.INSTANCE;
		final CompoundCommand cc = new CompoundCommand();
		cc.append(reassignElement(ed, vesselOrClass, ea));
		cc.append(SetCommand.create(ed, ea, AssignmentPackage.eINSTANCE.getElementAssignment_SpotIndex(), spotIndex));
		if (beforeTask != null) {
			final ElementAssignment ea2 = getElementAssignment(modelObject, beforeTask);
			if (ea2 != null) {
				final int newSeq = ea2.getSequence() + 1;
				System.err.println("Set seq of " + ea + " to " + newSeq);
				cc.append(SetCommand.create(ed, ea, AssignmentPackage.eINSTANCE.getElementAssignment_Sequence(), newSeq));
			}
		} else if (afterTask != null) {
			final ElementAssignment ea2 = getElementAssignment(modelObject, afterTask);
			if (ea2 != null) {
				final int newSeq = ea2.getSequence() - 1;
				System.err.println("Set seq of " + ea + " to " + newSeq);
				cc.append(SetCommand.create(ed, ea, AssignmentPackage.eINSTANCE.getElementAssignment_Sequence(), newSeq));
			}
		}

		return cc;
	}

	public static Command reassignElement(final EditingDomain ed, final AVesselSet<Vessel> destination, final ElementAssignment ea, final int maxSpot) {
		if (ea == null)
			return IdentityCommand.INSTANCE;

		final CompoundCommand cc = new CompoundCommand();

		cc.append(SetCommand.create(ed, ea, AssignmentPackage.eINSTANCE.getElementAssignment_Assignment(), destination == null ? SetCommand.UNSET_VALUE : destination));
		cc.append(SetCommand.create(ed, ea, AssignmentPackage.eINSTANCE.getElementAssignment_SpotIndex(), maxSpot));

		return cc;
	}

	public static int getMaxSpot(final AssignmentModel im) {
		int maxSpot = 0;
		for (final ElementAssignment ea : im.getElementAssignments()) {
			maxSpot = Math.max(maxSpot, ea.getSpotIndex());
		}
		maxSpot++;
		return maxSpot;
	}
}
