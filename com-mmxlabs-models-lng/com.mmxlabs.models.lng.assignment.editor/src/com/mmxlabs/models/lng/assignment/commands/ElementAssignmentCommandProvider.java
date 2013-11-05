/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.commands;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.common.commandservice.AbstractModelCommandProvider;
import com.mmxlabs.models.lng.assignment.AssignmentModel;
import com.mmxlabs.models.lng.assignment.ElementAssignment;
import com.mmxlabs.models.lng.assignment.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
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

public class ElementAssignmentCommandProvider extends AbstractModelCommandProvider<Object> {

	@Override
	public Command provideAdditionalCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet,
			final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {

		// These could change the cargo type!
		if (parameter.getFeature() == CargoPackage.Literals.CARGO__SLOTS || parameter.getFeature() == CargoPackage.Literals.SLOT__CARGO
				|| parameter.getFeature() == CargoPackage.Literals.LOAD_SLOT__DES_PURCHASE || parameter.getFeature() == CargoPackage.Literals.DISCHARGE_SLOT__FOB_SALE) {
			if (parameter.getOwner() instanceof UUIDObject) {
				return executeCommand(editingDomain, rootObject, (UUIDObject) parameter.getOwner());
			}
		}

		// Check for correct owner. For example we do not want to trigger this for cargoes being added to cargo groups.
		if (parameter.getOwner() == null || parameter.getOwner() instanceof CargoModel || parameter.getOwner() instanceof FleetModel || parameter.getOwner() instanceof ScenarioFleetModel) {
			if (commandClass == AddCommand.class || commandClass == SetCommand.class) {
				final CompoundCommand cmd = new CompoundCommand();
				for (final Object obj : collect(parameter)) {
					if (obj instanceof Cargo || obj instanceof Slot || obj instanceof VesselEvent) {
						cmd.append(executeCommand(editingDomain, rootObject, (UUIDObject) obj));
					}
				}
				if (!cmd.isEmpty()) {
					return cmd;
				}
			} else if (commandClass == DeleteCommand.class) {
				if (rootObject instanceof LNGScenarioModel) {

					final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
					final AssignmentModel assignmentModel = lngScenarioModel.getPortfolioModel().getAssignmentModel();

					final CompoundCommand cmd = new CompoundCommand();
					for (final Object obj : collect(parameter)) {
						if (obj instanceof Cargo || obj instanceof Slot || obj instanceof VesselEvent) {
							deleteAssignments(cmd, editingDomain, assignmentModel, (UUIDObject) obj);
						} else {
							Object target = null;
							if (obj instanceof VesselAvailability) {
								target = ((VesselAvailability) obj).getVessel();
							} else if (obj instanceof Vessel) {
								target = obj;
							}
							if (target != null) {
								for (final ElementAssignment ea : assignmentModel.getElementAssignments()) {
									if (ea.getAssignment() == target) {
										cmd.append(AssignmentEditorHelper.unassignElement(editingDomain, ea));
									}
								}
							}
						}
					}
					if (!cmd.isEmpty()) {
						return cmd;
					}
				}
			}
		}
		// else if (commandClass == SetCommand.class) {
		//
		// if (fe)
		//
		// Object owner = parameter.getOwner();
		// if (overrides.containsKey(owner)) {
		// owner = overrides.get(owner);
		// }
		//
		// if (owner instanceof LoadSlot) {
		// final LoadSlot loadSlot = (LoadSlot) owner;
		// return executeCommand(editingDomain, rootObject, loadSlot);
		// } else if (owner instanceof DischargeSlot) {
		// final DischargeSlot dischargeSlot = (DischargeSlot) owner;
		// return executeCommand(editingDomain, rootObject, dischargeSlot);
		// } else if (owner instanceof Cargo) {
		// final Cargo cargo = (Cargo) owner;
		// return executeCommand(editingDomain, rootObject, cargo);
		// } else if (owner instanceof VesselEvent) {
		// final VesselEvent vesselEvent = (VesselEvent) owner;
		// return executeCommand(editingDomain, rootObject, vesselEvent);
		// }
		// }
		return null;
	}

	public Command executeCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final UUIDObject assignedObject) {
		if (rootObject instanceof LNGScenarioModel) {
			final AssignmentModel assignmentModel = ((LNGScenarioModel) rootObject).getPortfolioModel().getAssignmentModel();
			return new UpdateElementAssignmentCommand(editingDomain, assignmentModel, assignedObject);
		}
		return null;
	}

	/**
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Collection<Object> collect(final CommandParameter parameter) {
		if (parameter.getCollection() != null) {
			return (Collection<Object>) parameter.getCollection();
		} else if (parameter.getValue() != null) {
			return Collections.singleton(parameter.getValue());
		}
		return Collections.emptySet();
	}

	private void deleteAssignments(final CompoundCommand cmd, final EditingDomain domain, final AssignmentModel assignmentModel, final UUIDObject uuidObject) {

		for (final ElementAssignment ea : assignmentModel.getElementAssignments()) {
			if (ea != null) {
				// General clean up...
				if (ea.getAssignedObject() == null && ea.getAssignment() == null) {
					cmd.append(DeleteCommand.create(domain, ea));
				}

				// Real check
				if (ea.getAssignedObject() == uuidObject) {
					cmd.append(DeleteCommand.create(domain, ea));
				}
			}
		}
	}
}
