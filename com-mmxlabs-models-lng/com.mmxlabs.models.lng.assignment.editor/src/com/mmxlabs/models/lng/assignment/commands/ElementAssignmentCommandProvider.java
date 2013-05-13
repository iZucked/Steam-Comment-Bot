/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.commands;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.common.commandservice.BaseModelCommandProvider;
import com.mmxlabs.models.lng.assignment.AssignmentFactory;
import com.mmxlabs.models.lng.assignment.AssignmentModel;
import com.mmxlabs.models.lng.assignment.AssignmentPackage;
import com.mmxlabs.models.lng.assignment.ElementAssignment;
import com.mmxlabs.models.lng.assignment.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.ScenarioFleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselAvailability;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

public class ElementAssignmentCommandProvider extends BaseModelCommandProvider<Object> {

	@Override
	public Command provideAdditionalCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet,
			final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {
		// Check for correct owner. For example we do not want to trigger this for cargoes being added to cargo groups.
		if (parameter.getOwner() == null || parameter.getOwner() instanceof CargoModel || parameter.getOwner() instanceof FleetModel|| parameter.getOwner() instanceof ScenarioFleetModel) {
			return super.provideAdditionalCommand(editingDomain, rootObject, overrides, editSet, commandClass, parameter, input);
		}

		return null;
	}

	@Override
	protected boolean shouldHandleAddition(final Object addedObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet) {
		if (addedObject instanceof Cargo || addedObject instanceof VesselEvent) {

			for (final EObject entry : editSet) {
				if (entry instanceof ElementAssignment) {
					final ElementAssignment elementAssignment = (ElementAssignment) entry;
					if (elementAssignment.getAssignedObject() == addedObject) {
						// There is already an assignment under consideration.
						return false;
					}
				}
			}

			return true;
		}

		return false;
	}

	@Override
	protected boolean shouldHandleDeletion(final Object deletedObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet) {
		return (deletedObject instanceof Cargo || deletedObject instanceof VesselEvent || deletedObject instanceof VesselAvailability);
	}

	@Override
	protected Command objectAdded(final EditingDomain domain, final MMXRootObject rootObject, final Object added, final Map<EObject, EObject> overrides, final Set<EObject> editSet) {
		if (rootObject instanceof LNGScenarioModel) {

			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final ElementAssignment ea = AssignmentFactory.eINSTANCE.createElementAssignment();
			ea.setAssignedObject((UUIDObject) added);
			return AddCommand.create(domain, lngScenarioModel.getPortfolioModel().getAssignmentModel(), AssignmentPackage.eINSTANCE.getAssignmentModel_ElementAssignments(), ea);
		}
		return null;
	}

	@Override
	protected Command objectDeleted(final EditingDomain domain, final MMXRootObject rootObject, final Object deleted, final Map<EObject, EObject> overrides, final Set<EObject> editSet) {
		if (rootObject instanceof LNGScenarioModel) {
			final AssignmentModel assignmentModel = ((LNGScenarioModel) rootObject).getPortfolioModel().getAssignmentModel();

			// if a vessel availability was deleted, unassign any associated element assignment
			if (deleted instanceof VesselAvailability) {
				final Vessel vessel = ((VesselAvailability) deleted).getVessel();
				final CompoundCommand cmd = new CompoundCommand("Remove element assignents for " + vessel.getName());
				for (final ElementAssignment ea : assignmentModel.getElementAssignments()) {
					if (ea.getAssignment() == vessel) {
						cmd.append(AssignmentEditorHelper.unassignElement(domain, ea));
					}
				}

				if (!cmd.isEmpty()) {
					return cmd;
				}
			}
			// if a cargo or vessel event was deleted, delete any associated element assignment
			else if (deleted instanceof Cargo || deleted instanceof VesselEvent) {
				final ElementAssignment ea = AssignmentEditorHelper.getElementAssignment(assignmentModel, (UUIDObject) deleted);
				if (ea != null) {
					return DeleteCommand.create(domain, ea);
				}
			}

		}
		return null;
	}

}
