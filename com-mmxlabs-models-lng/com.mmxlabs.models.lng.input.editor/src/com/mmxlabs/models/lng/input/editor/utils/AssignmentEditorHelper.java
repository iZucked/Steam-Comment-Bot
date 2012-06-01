package com.mmxlabs.models.lng.input.editor.utils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.management.timer.Timer;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.input.Assignment;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.InputPackage;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.mmxcore.UUIDObject;

public class AssignmentEditorHelper {
	public static Date getStartDate(UUIDObject task) {
		if (task instanceof Cargo) {
			return ((Cargo) task).getLoadSlot()
					.getWindowStartWithSlotOrPortTime();
		} else if (task instanceof VesselEvent) {
			return
					((VesselEvent) task).getStartBy();
		} else if (task instanceof Slot) {
			return ((Slot) task).getWindowStartWithSlotOrPortTime();
		} else {
			return null;
		}
	}
	
	public static Date getEndDate(UUIDObject task) {
		if (task instanceof Cargo) {
			return ((Cargo) task).getDischargeSlot().getWindowEndWithSlotOrPortTime();
		} else if (task instanceof VesselEvent) {
			return 
					new Date(
					((VesselEvent) task).getStartBy().getTime()
					+ Timer.ONE_DAY * ((VesselEvent)task).getDurationInDays());
		} else if (task instanceof Slot) {
			return ((Slot) task).getWindowEndWithSlotOrPortTime();
		} else {
			return null;
		}
	}
	
	public static Command taskReassigned(final EditingDomain ed, final InputModel modelObject, UUIDObject task, UUIDObject beforeTask,
			UUIDObject afterTask, Assignment oldResource,
			Assignment newResource) {
		final CompoundCommand cc = new CompoundCommand();
		
		// this should definitely kill anything pre-existing
		Command totallyUnassign = totallyUnassign(ed, modelObject, task);
		cc.append(totallyUnassign);
		
		// copy the current state and remove all ocurrences of task, in order that when we get
		// the position it's valid after the unassign command has executed.
		final List<UUIDObject> assigned = new ArrayList<UUIDObject>(newResource.getAssignedObjects());
		assigned.removeAll(Collections.singleton(task));
		
		int position;
		if (beforeTask != null) {
			position = assigned.indexOf(beforeTask);
		} else if (afterTask != null) {
			position = assigned.indexOf(afterTask) + 1;
		} else {
			position = 0;
			final DateFormat df = DateFormat.getDateInstance();
			
			final Date start = getStartDate(task);
			final Date end = getEndDate(task);

			System.err.println("Insert " + df.format(start) + "-" + df.format(end));
			
			if (start != null && end != null) {
				for (final UUIDObject o : assigned) {
					final Date startO = getStartDate(o);
					final Date endO = getEndDate(o);
					System.err.println("Look at " + df.format(startO) + "-" + df.format(endO));
					if (end.before(startO)) {
						System.err.println("Insert before this");
						break;
					} else if (start.after(endO)) {
						position++;
						System.err.println("Insert after this");
						break;
					} else {
						position++;
					}
				}
			}
			
			System.err.println("Insert before " + position);
		}
		
		if (newResource.getAssignedObjects().isEmpty() || position == newResource.getAssignedObjects().size()) {
			cc.append(AddCommand.create(ed, newResource, InputPackage.eINSTANCE.getAssignment_AssignedObjects(), task));
		} else {
			cc.append(AddCommand.create(ed, newResource, InputPackage.eINSTANCE.getAssignment_AssignedObjects(), task, position));
		}
		
		return cc;
	}

	public static Command taskUnassigned(final EditingDomain ed, final InputModel modelObject, UUIDObject task, Assignment oldResource) {
		final CompoundCommand cc = new CompoundCommand();
		cc.append(RemoveCommand.create(ed, oldResource, InputPackage.eINSTANCE.getAssignment_AssignedObjects(), task));
		cc.append(RemoveCommand.create(ed, modelObject, InputPackage.eINSTANCE.getInputModel_LockedAssignedObjects(), task));
		return cc;
	}
	
	public static Command totallyUnassign(final EditingDomain ed, final InputModel modelObject, final UUIDObject task) {
		final CompoundCommand kill = new CompoundCommand();
		kill.append(IdentityCommand.INSTANCE);
		for (final Assignment a : modelObject.getAssignments()) {
			for (final UUIDObject b : a.getAssignedObjects()) {
				if (b == task) {
					kill.append(RemoveCommand.create(ed, a, InputPackage.eINSTANCE.getAssignment_AssignedObjects(), task));
				}
			}
		}
		return kill;
	}
	
	public static Assignment getAssignmentForTask(final InputModel inputModel, final UUIDObject object) {
		for (final Assignment a : inputModel.getAssignments()) {
			if (a.getAssignedObjects().contains(object)) {
				return a;
			}
		}
		return null;
	}
	
	public static Assignment getAssignmentForVessel(final InputModel input, final AVesselSet set) {
		for (final Assignment a : input.getAssignments()) {
			if (a.getVessels().contains(set)) {
				return a;
			}
		}
		return null;
	}
}
