/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
import com.mmxlabs.models.lng.types.VesselAssignmentType;

/**
 */
public class AssignableElementCommandHelper {

	public static Command reassignElement(final EditingDomain ed, final AssignableElement beforeTask, final AssignableElement task, final AssignableElement afterTask,
			final VesselAssignmentType vesselOrMarket, final int spotIndex) {
		final CompoundCommand cc = new CompoundCommand();
		cc.append(SetCommand.create(ed, task, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE, vesselOrMarket == null ? SetCommand.UNSET_VALUE : vesselOrMarket));
		cc.append(SetCommand.create(ed, task, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, spotIndex));
		if (beforeTask != null) {
			final int newSeq = beforeTask.getSequenceHint() + 1;
			cc.append(SetCommand.create(ed, task, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SEQUENCE_HINT, newSeq));
		} else if (afterTask != null) {
			final int newSeq = afterTask.getSequenceHint() - 1;
			cc.append(SetCommand.create(ed, afterTask, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SEQUENCE_HINT, newSeq));
		}

		return cc;
	}

	public static Command reassignElement(final EditingDomain ed, VesselAssignmentType vesselOrMarket, final AssignableElement ea, final int maxSpot) {
		if (ea == null)
			return IdentityCommand.INSTANCE;

		final CompoundCommand cc = new CompoundCommand();

		cc.append(SetCommand.create(ed, ea, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE, vesselOrMarket == null ? SetCommand.UNSET_VALUE : vesselOrMarket));
		cc.append(SetCommand.create(ed, ea, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX, maxSpot));

		return cc;
	}

}
