/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.commands;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.common.commandservice.AbstractModelCommandProvider;
import com.mmxlabs.models.common.commandservice.IModelCommandProvider;
import com.mmxlabs.models.lng.assignment.AssignmentModel;
import com.mmxlabs.models.lng.assignment.ElementAssignment;
import com.mmxlabs.models.lng.assignment.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.ui.util.SpotSlotHelper;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * 
 * A {@link IModelCommandProvider} implementation to map DES/FOB details between slots
 * 
 * @author Simon Goodall
 * 
 */
public class CargoTypeUpdatingCommandProvider extends AbstractModelCommandProvider<Set<EObject>> {
	@Override
	public Command provideAdditionalCommand(final EditingDomain editingDomain, final MMXRootObject rootObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet,
			final Class<? extends Command> commandClass, final CommandParameter parameter, final Command input) {
		
		if (true) {
			return null;
		}
		
		if (commandClass == SetCommand.class) {
			{
				Set<EObject> seenObjects = getContext();
				if (seenObjects == null) {
					seenObjects = new HashSet<EObject>();
					setContext(seenObjects);
				}

				if (seenObjects.contains(parameter.getEOwner())) {
					return null;
				}
				seenObjects.add(parameter.getEOwner());
			}

			if (rootObject instanceof LNGScenarioModel) {

				final AssignmentModel assignmentModel = ((LNGScenarioModel) rootObject).getPortfolioModel().getAssignmentModel();

				if (parameter.getEOwner() instanceof LoadSlot) {
					final LoadSlot slot = (LoadSlot) parameter.getEOwner();

					if (slot.getCargo() != null) {
						final Cargo cargo = slot.getCargo();
						final ElementAssignment assignment = getAssignmentForCargo(overrides, assignmentModel, cargo);
						DischargeSlot dischargeSlot = null;
						for (final Slot slot2 : cargo.getSlots()) {
							if (slot2 instanceof DischargeSlot) {
								dischargeSlot = (DischargeSlot) slot2;
								break;
							}
						}
						if (dischargeSlot != null) {

							if (parameter.getEStructuralFeature() == CargoPackage.eINSTANCE.getLoadSlot_DESPurchase()) {
								final boolean desPurchase = (Boolean) parameter.getValue();
								if (desPurchase) {

									final CompoundCommand cmd = new CompoundCommand("Convert to DES Purchase");
									cmd.append(AssignmentEditorHelper.unassignElement(editingDomain, assignment));
									cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getLoadSlot_ArriveCold(), false));
									cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_Port(), dischargeSlot.getPort()));
									cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_Duration(), 0));

									if (slot instanceof SpotSlot) {
										SpotSlotHelper.setSpotSlotTimeWindow(editingDomain, slot, dischargeSlot, cmd);
									} else {
										cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_WindowStart(), dischargeSlot.getWindowStart()));
										if (dischargeSlot.isSetWindowStartTime()) {
											cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_WindowStartTime(), dischargeSlot.getWindowStartTime()));
										} else {
											cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_WindowStartTime(), SetCommand.UNSET_VALUE));
										}
									}
									return cmd;
								}
							} else if (dischargeSlot.isFOBSale()) {
								if (parameter.getEStructuralFeature() == CargoPackage.eINSTANCE.getSlot_Port() || parameter.getEStructuralFeature() == CargoPackage.eINSTANCE.getSlot_WindowStart()) {
									final CompoundCommand cmd = new CompoundCommand("FOB Sale update");

									if (dischargeSlot instanceof SpotSlot) {
										final Port port = (parameter.getEStructuralFeature() == CargoPackage.eINSTANCE.getSlot_Port()) ? ((Port) parameter.getValue()) : slot.getPort();
										final Date date = (parameter.getEStructuralFeature() == CargoPackage.eINSTANCE.getSlot_WindowStart()) ? ((Date) parameter.getValue()) : slot.getWindowStart();
										if (parameter.getEStructuralFeature() == CargoPackage.eINSTANCE.getSlot_Port()) {
											cmd.append(SetCommand.create(editingDomain, dischargeSlot, parameter.getEStructuralFeature(), parameter.getValue()));
										}
										SpotSlotHelper.setSpotSlotTimeWindow(editingDomain, dischargeSlot, slot.getPort(), port, date, cmd);
									} else {
										cmd.append(SetCommand.create(editingDomain, dischargeSlot, parameter.getEStructuralFeature(), parameter.getValue()));
									}

									return cmd;
								}
							}
						}
					}
					// Fall through to Spot Slots which are not DES Purchases / FOB Sales
					if (parameter.getEOwner() instanceof SpotSlot) {
						if (parameter.getEStructuralFeature() == CargoPackage.eINSTANCE.getSlot_WindowStart()) {

							final CompoundCommand cmd = new CompoundCommand("Update spot slot date range");
							final Date newDate = (Date) parameter.getValue();
							SpotSlotHelper.setSpotSlotTimeWindow(editingDomain, slot, slot.getPort(), slot.getPort(), newDate, cmd);
							if (cmd.isEmpty()) {
								return null;
							}

							return cmd.unwrap();
						}
					}

				}
				if (parameter.getEOwner() instanceof DischargeSlot) {
					final DischargeSlot slot = (DischargeSlot) parameter.getEOwner();
					if (slot.getCargo() != null) {
						final Cargo cargo = slot.getCargo();

						final ElementAssignment assignment = getAssignmentForCargo(overrides, assignmentModel, cargo);
						LoadSlot loadSlot = null;
						for (final Slot slot2 : cargo.getSlots()) {
							if (slot2 instanceof LoadSlot) {
								loadSlot = (LoadSlot) slot2;
								break;
							}
						}
						if (loadSlot != null) {

							if (parameter.getEStructuralFeature() == CargoPackage.eINSTANCE.getDischargeSlot_FOBSale()) {
								final boolean fobSale = (Boolean) parameter.getValue();
								if (fobSale) {

									final CompoundCommand cmd = new CompoundCommand("Convert to FOB Sale");
									cmd.append(AssignmentEditorHelper.unassignElement(editingDomain, assignment));
									cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_Port(), loadSlot.getPort()));
									cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_Duration(), 0));

									if (slot instanceof SpotSlot) {
										SpotSlotHelper.setSpotSlotTimeWindow(editingDomain, slot, loadSlot, cmd);
									} else {
										cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_WindowStart(), loadSlot.getWindowStart()));
										if (loadSlot.isSetWindowStartTime()) {
											cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_WindowStartTime(), loadSlot.getWindowStartTime()));
										} else {
											cmd.append(SetCommand.create(editingDomain, slot, CargoPackage.eINSTANCE.getSlot_WindowStartTime(), SetCommand.UNSET_VALUE));
										}
									}
									return cmd;
								}
							} else if (loadSlot.isDESPurchase()) {
								if (parameter.getEStructuralFeature() == CargoPackage.eINSTANCE.getSlot_Port() || parameter.getEStructuralFeature() == CargoPackage.eINSTANCE.getSlot_WindowStart()) {
									final CompoundCommand cmd = new CompoundCommand("DES Purchase update");

									if (loadSlot instanceof SpotSlot) {
										final Port port = (parameter.getEStructuralFeature() == CargoPackage.eINSTANCE.getSlot_Port()) ? ((Port) parameter.getValue()) : slot.getPort();
										final Date date = (parameter.getEStructuralFeature() == CargoPackage.eINSTANCE.getSlot_WindowStart()) ? ((Date) parameter.getValue()) : slot.getWindowStart();

										if (parameter.getEStructuralFeature() == CargoPackage.eINSTANCE.getSlot_Port()) {
											cmd.append(SetCommand.create(editingDomain, loadSlot, parameter.getEStructuralFeature(), parameter.getValue()));
										}
										SpotSlotHelper.setSpotSlotTimeWindow(editingDomain, loadSlot, slot.getPort(), port, date, cmd);

									} else {
										cmd.append(SetCommand.create(editingDomain, loadSlot, parameter.getEStructuralFeature(), parameter.getValue()));
									}
									return cmd;
								}
							}

						}
					}
				}

				// Fall through to Spot Slots which are not DES Purchases / FOB Sales
				if (parameter.getEOwner() instanceof SpotSlot) {
					final Slot slot = (Slot) parameter.getEOwner();
					if (parameter.getEStructuralFeature() == CargoPackage.eINSTANCE.getSlot_WindowStart()) {

						final CompoundCommand cmd = new CompoundCommand("Update spot slot date range");
						final Date newDate = (Date) parameter.getValue();
						SpotSlotHelper.setSpotSlotTimeWindow(editingDomain, slot, slot.getPort(), slot.getPort(), newDate, cmd);
						if (cmd.isEmpty()) {
							return null;
						}

						return cmd.unwrap();
					}
				}

			}
		}
		return null;
	}

	private ElementAssignment getAssignmentForCargo(final Map<EObject, EObject> overrides, final AssignmentModel assignmentModel, Cargo cargo) {

		// Find the assignment for the Cargo

		// Cargo might be duplicated, so we need to find the original...
		if (overrides.containsValue(cargo)) {
			for (final Map.Entry<EObject, EObject> entry : overrides.entrySet()) {
				if (entry.getValue() == cargo) {
					cargo = (Cargo) entry.getKey();
					break;
				}
			}
		}
		// Look up the assignment
		ElementAssignment assignment = AssignmentEditorHelper.getElementAssignment(assignmentModel, cargo);

		// Look up the override, if present
		if (overrides.containsKey(assignment)) {
			assignment = (ElementAssignment) overrides.get(assignment);
		}
		return assignment;
	}
}
