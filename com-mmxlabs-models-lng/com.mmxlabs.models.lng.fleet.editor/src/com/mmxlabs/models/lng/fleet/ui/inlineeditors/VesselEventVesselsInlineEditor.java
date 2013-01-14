/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.inlineeditors;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.input.ElementAssignment;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.impl.MultiReferenceInlineEditor;

public class VesselEventVesselsInlineEditor extends MultiReferenceInlineEditor {

	private IScenarioEditingLocation location;
	private Collection<EObject> range;

	public VesselEventVesselsInlineEditor(final EStructuralFeature feature) {
		super(feature);
	}

	@Override
	public void display(final IScenarioEditingLocation location, final MMXRootObject context, final EObject input, final Collection<EObject> range) {
		this.location = location;
		this.range = range;
		super.display(location, context, input, range);
	}

	@Override
	protected Command createSetCommand(final Object value) {
		final CompoundCommand cmd = new CompoundCommand("Edit event vessels");
		cmd.append(super.createSetCommand(value));

		// Might be a duplicate..
		ElementAssignment elementAssignment = null;
		for (final EObject r : range) {
			if (r instanceof ElementAssignment) {
				elementAssignment = (ElementAssignment) r;
			}
		}
		// .. otherwise lookup the assignment
		if (elementAssignment == null) {
			final InputModel inputModel = location.getRootObject().getSubModel(InputModel.class);
			elementAssignment = AssignmentEditorHelper.getElementAssignment(inputModel, (UUIDObject) input);
		}
		final List<?> list = (List<?>) value;
		if (elementAssignment != null) {
			if (list.size() == 1) {
				final Object newAssignment = list.get(0);
				cmd.append(AssignmentEditorHelper.reassignElement(this.commandHandler.getEditingDomain(), (AVesselSet) newAssignment, elementAssignment));
			} else {
				if (!list.contains(elementAssignment.getAssignment())) {
					cmd.append(AssignmentEditorHelper.unassignElement(this.commandHandler.getEditingDomain(), elementAssignment));
				}
			}
		}

		return cmd.unwrap();
	}
}
