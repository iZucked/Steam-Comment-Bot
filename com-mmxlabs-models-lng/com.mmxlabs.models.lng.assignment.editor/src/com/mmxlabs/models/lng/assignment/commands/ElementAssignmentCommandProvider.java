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
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
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

		// These could change the cargo type!
		if (parameter.getFeature() == CargoPackage.Literals.CARGO__SLOTS || parameter.getFeature() == CargoPackage.Literals.SLOT__CARGO) {
			// TODO: Here we need to see what state the cargo will be in after command execution and determine whether or not to update the element assignment.
			// TODO: However this may occur before other commands being executed so we may not be creating the correct thing...
			// TODO: We probably need a custom command to check the current state during command execution rather than creation
			
//			if (parameter.getOwner() instanceof Cargo) {
//				Cargo cargo = (Cargo) parameter.getOwner();
//				return updateCargoType(editingDomain, rootObject, overrides, editSet, cargo);
//			} else if (parameter.getOwner() instanceof Slot) {
//				Slot slot = (Slot) parameter.getOwner();
//				if (parameter.get)
//				if (cargo.getCargoType() == CargoType.FLEET) {
//					return objectAdded(editingDomain, rootObject, cargo, overrides, editSet);
//				} else {
//					return objectDeleted(editingDomain, rootObject, cargo, overrides, editSet);
//				}
//			}
		}

		// Check for correct owner. For example we do not want to trigger this for cargoes being added to cargo groups.
		if (parameter.getOwner() == null || parameter.getOwner() instanceof CargoModel || parameter.getOwner() instanceof FleetModel || parameter.getOwner() instanceof ScenarioFleetModel) {
			return super.provideAdditionalCommand(editingDomain, rootObject, overrides, editSet, commandClass, parameter, input);
		}

		else if (parameter.getOwner() instanceof LoadSlot) {
			LoadSlot loadSlot = (LoadSlot) parameter.getOwner();
			if (loadSlot.isDESPurchase()) {
				return super.provideAdditionalCommand(editingDomain, rootObject, overrides, editSet, commandClass, parameter, input);
			}
		}

		else if (parameter.getOwner() instanceof DischargeSlot) {
			DischargeSlot dischargeSlot = (DischargeSlot) parameter.getOwner();
			if (dischargeSlot.isFOBSale()) {
				return super.provideAdditionalCommand(editingDomain, rootObject, overrides, editSet, commandClass, parameter, input);
			}
		}

		return null;
	}

	public Command updateCargoType(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet, Cargo cargo) {
		if (cargo.getCargoType() == CargoType.FLEET) {
			return objectAdded(editingDomain, rootObject, cargo, overrides, editSet);
		} else {
			return objectDeleted(editingDomain, rootObject, cargo, overrides, editSet);
		}
	}

	@Override
	protected boolean shouldHandleAddition(final Object addedObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet) {

		boolean checkObject = false;

		checkObject = addedObject instanceof VesselEvent;
		if (!checkObject && addedObject instanceof Cargo) {
			Cargo cargo = (Cargo) addedObject;
			checkObject = cargo.getCargoType() == CargoType.FLEET;
		}
		if (!checkObject && addedObject instanceof LoadSlot) {
			LoadSlot loadSlot = (LoadSlot) addedObject;
			checkObject = loadSlot.isDESPurchase();
		}
		if (!checkObject && addedObject instanceof DischargeSlot) {
			DischargeSlot DischargeSlot = (DischargeSlot) addedObject;
			checkObject = DischargeSlot.isFOBSale();
		}

		if (checkObject) {

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
		boolean shouldHandleDeletion = (deletedObject instanceof Cargo || deletedObject instanceof VesselEvent || deletedObject instanceof VesselAvailability);

		if (!shouldHandleDeletion && deletedObject instanceof LoadSlot) {
			LoadSlot loadSlot = (LoadSlot) deletedObject;
			shouldHandleDeletion = loadSlot.isDESPurchase();
		}
		if (!shouldHandleDeletion && deletedObject instanceof DischargeSlot) {
			DischargeSlot DischargeSlot = (DischargeSlot) deletedObject;
			shouldHandleDeletion = DischargeSlot.isFOBSale();
		}
		return shouldHandleDeletion;
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
			else if (deleted instanceof Cargo || deleted instanceof VesselEvent || deleted instanceof Slot) {
				final ElementAssignment ea = AssignmentEditorHelper.getElementAssignment(assignmentModel, (UUIDObject) deleted);
				if (ea != null) {
					return DeleteCommand.create(domain, ea);
				}
			}

		}
		return null;
	}

}
