/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.edit.utils;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.types.AVesselSet;

/**
 */
public class AssignableElementCommandHelper {

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

}
