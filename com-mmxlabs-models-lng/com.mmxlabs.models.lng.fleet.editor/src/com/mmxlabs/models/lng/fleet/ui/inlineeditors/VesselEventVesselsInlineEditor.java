/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.inlineeditors;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;

import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.MultiReferenceInlineEditor;

public class VesselEventVesselsInlineEditor extends MultiReferenceInlineEditor {

	//private IScenarioEditingLocation location;
	private IDialogEditingContext dialogContext;
	private Collection<EObject> range;

	public VesselEventVesselsInlineEditor(final EStructuralFeature feature) {
		super(feature);
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject context, final EObject input, final Collection<EObject> range) {
		this.dialogContext = dialogContext;
		this.range = range;
		super.display(dialogContext, context, input, range);
	}

	@Override
	protected Command createSetCommand(final Object value) {
		final CompoundCommand cmd = new CompoundCommand("Edit event vessels");
		cmd.append(super.createSetCommand(value));

		// Might be a duplicate..
		AssignableElement elementAssignment = null;
		for (final EObject r : range) {
			if (r instanceof AssignableElement) {
				elementAssignment = (AssignableElement) r;
			}
		}
		final List<?> list = (List<?>) value;
		if (elementAssignment != null) {
			if (list.size() == 1) {
				final Object newAssignment = list.get(0);
				cmd.append(SetCommand.create(this.commandHandler.getEditingDomain(), elementAssignment, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE, newAssignment));
			} else {
				if (!list.contains(elementAssignment.getVesselAssignmentType())) {
					cmd.append(SetCommand.create(this.commandHandler.getEditingDomain(), elementAssignment, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE, SetCommand.UNSET_VALUE));
				}
			}
		}

		return cmd.unwrap();
	}
}
