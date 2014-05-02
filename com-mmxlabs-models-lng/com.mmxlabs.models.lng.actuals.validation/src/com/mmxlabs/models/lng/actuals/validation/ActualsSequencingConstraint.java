/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.actuals.validation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.actuals.ActualsModel;
import com.mmxlabs.models.lng.actuals.CargoActuals;
import com.mmxlabs.models.lng.actuals.DischargeActuals;
import com.mmxlabs.models.lng.actuals.LoadActuals;
import com.mmxlabs.models.lng.actuals.SlotActuals;
import com.mmxlabs.models.lng.actuals.validation.internal.Activator;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.cargo.editor.utils.CollectedAssignment;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 * This constraint ensures that an {@link CargoActuals} is preceeded by another {@link CargoActuals}, or the vessel start. (or a vessel event with warning). This ensures proper heel tracking etc.
 * 
 * @author Simon Goodall
 * 
 */
public class ActualsSequencingConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		final MMXRootObject rootObject = Activator.getDefault().getExtraValidationContext().getRootObject();
		if (target instanceof ActualsModel) {
			ActualsModel actualsModel = (ActualsModel) target;
			final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;
			final FleetModel fleetModel = scenarioModel.getFleetModel();
			final CargoModel cargoModel = scenarioModel.getPortfolioModel().getCargoModel();

			// Build up lookup tables
			final Map<Cargo, CargoActuals> cargoActualsMap = new HashMap<>();
			final Map<Slot, SlotActuals> slotActualsMap = new HashMap<>();
			final Map<LoadSlot, LoadActuals> loadActualsMap = new HashMap<>();
			final Map<DischargeSlot, DischargeActuals> dischargeActualsMap = new HashMap<>();

			for (final CargoActuals cargoActuals : actualsModel.getCargoActuals()) {
				final Cargo cargo = cargoActuals.getCargo();
				// Skip broken date
				if (cargo == null) {
					continue;
				}
				cargoActualsMap.put(cargo, cargoActuals);

				for (final Slot slot : cargo.getSlots()) {
					for (final SlotActuals slotActuals : cargoActuals.getActuals()) {
						if (slot == slotActuals.getSlot()) {
							slotActualsMap.put(slot, slotActuals);
							if (slot instanceof LoadSlot && slotActuals instanceof LoadActuals) {
								loadActualsMap.put((LoadSlot) slot, (LoadActuals) slotActuals);
							} else if (slot instanceof DischargeSlot && slotActuals instanceof DischargeActuals) {
								dischargeActualsMap.put((DischargeSlot) slot, (DischargeActuals) slotActuals);
							}
						}
					}
				}
			}

			final List<CollectedAssignment> collectAssignments = AssignmentEditorHelper.collectAssignments(cargoModel, fleetModel);

			// Check sequencing for each grouping
			for (final CollectedAssignment collectedAssignment : collectAssignments) {

				// Assume vessel start is Actualised
				boolean previousElementHasActuals = true;
				AssignableElement prevObject = null;

				for (final AssignableElement assignment : collectedAssignment.getAssignedObjects()) {
					boolean currentElementHasActuals = false;
					if (assignment instanceof Cargo) {
						final Cargo cargo = (Cargo) assignment;
						if (cargoActualsMap.containsKey(cargo)) {
							currentElementHasActuals = true;
						}
					} else if (assignment instanceof VesselEvent) {
						currentElementHasActuals = false;
					}

					if (prevObject != null && previousElementHasActuals == false && currentElementHasActuals == true) {
						// Only warn about previous missing actuals if an event.
						int status = prevObject instanceof VesselEvent ? IStatus.WARNING : IStatus.ERROR;

						final String msg = String.format("Cargo %s is not preceeded by another actualised event.", getID(prevObject));

						final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg), status);
						failure.addEObjectAndFeature(prevObject, MMXCorePackage.Literals.NAMED_OBJECT__NAME);
						failure.addEObjectAndFeature(assignment, MMXCorePackage.Literals.NAMED_OBJECT__NAME);
						failure.addEObjectAndFeature(cargoActualsMap.get(assignment), MMXCorePackage.Literals.NAMED_OBJECT__NAME);

						statuses.add(failure);
					}

					prevObject = assignment;
					previousElementHasActuals = currentElementHasActuals;
				}
			}

		}
		return Activator.PLUGIN_ID;
	}

	private String getID(final EObject target) {
		if (target instanceof Cargo) {
			final Cargo slot = (Cargo) target;
			return "cargo \"" + slot.getName() + "\"";
		} else if (target instanceof Slot) {
			final Slot slot = (Slot) target;
			return "slot \"" + slot.getName() + "\"";
		} else if (target instanceof VesselEvent) {
			final VesselEvent vesselEvent = (VesselEvent) target;
			return "event \"" + vesselEvent.getName() + "\"";
		}
		return "(unknown)";
	}

}
