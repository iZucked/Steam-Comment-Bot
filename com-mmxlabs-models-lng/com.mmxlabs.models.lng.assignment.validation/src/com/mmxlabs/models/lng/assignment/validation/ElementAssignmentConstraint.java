/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.assignment.AssignmentPackage;
import com.mmxlabs.models.lng.assignment.ElementAssignment;
import com.mmxlabs.models.lng.assignment.validation.internal.Activator;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.ScenarioFleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselAvailability;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * Validation constraint to check the type of vessel assigned. FOB/DES cargoes are either unassigned or a vessel not part of the "scenario" data - that is vessels in the FleetModel but do not have a
 * VesselAvailability. Fleet cargoes can be only scenario vessels or vessel class assignments. Vessel Events can only use scenario vessels.
 * 
 */
public class ElementAssignmentConstraint extends AbstractModelMultiConstraint {
	@Override
	public String validate(final IValidationContext ctx, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		if (object instanceof ElementAssignment) {
			final ElementAssignment elementAssignment = (ElementAssignment) object;

			final UUIDObject uuidObject = elementAssignment.getAssignedObject();
			if (elementAssignment.getAssignment() == null) {
				return Activator.PLUGIN_ID;
			}

			if (!isValidObject(uuidObject)) {
				final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format(
						"Element Assignment has unexpected assigned object of type %s.", uuidObject.eClass().getName())));
				failure.addEObjectAndFeature(elementAssignment, AssignmentPackage.eINSTANCE.getElementAssignment_AssignedObject());

				failures.add(failure);
			}

			final AVesselSet<Vessel> vessel = elementAssignment.getAssignment();

			Cargo cargo = null;
			Slot slot = null;
			VesselEvent event = null;
			if (uuidObject instanceof VesselEvent) {
				event = (VesselEvent) uuidObject;
			} else if (uuidObject instanceof LoadSlot) {
				slot = (LoadSlot) uuidObject;
				cargo = slot.getCargo();
			} else if (uuidObject instanceof DischargeSlot) {
				slot = (DischargeSlot) uuidObject;
				cargo = slot.getCargo();
			} else if (uuidObject instanceof Cargo) {
				cargo = (Cargo) uuidObject;
			}

			final Set<Vessel> scenarioVessels = new HashSet<>();
			final IExtraValidationContext extraValidationContext = Activator.getDefault().getExtraValidationContext();
			final MMXRootObject rootObject = extraValidationContext.getRootObject();
			if (rootObject instanceof LNGScenarioModel) {
				final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
				final LNGPortfolioModel portfolioModel = lngScenarioModel.getPortfolioModel();
				final ScenarioFleetModel scenarioFleetModel = portfolioModel.getScenarioFleetModel();
				for (final VesselAvailability va : scenarioFleetModel.getVesselAvailabilities()) {
					scenarioVessels.add(va.getVessel());
				}
			}

			if (event != null) {

				if (!scenarioVessels.contains(vessel)) {
					final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Vessel event " + event.getName()
							+ " is assigned to non-scenario vessel " + vessel.getName()));
					failure.addEObjectAndFeature(elementAssignment, AssignmentPackage.Literals.ELEMENT_ASSIGNMENT__ASSIGNMENT);

					failures.add(failure);
				}
			} else if (cargo != null) {

				if (cargo.getCargoType() == CargoType.FLEET) {
					if (vessel instanceof Vessel) {
						if (!scenarioVessels.contains(vessel)) {
							final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Fleet cargo " + cargo.getName()
									+ " is assigned to non-scenario vessel " + vessel.getName()));
							failure.addEObjectAndFeature(elementAssignment, AssignmentPackage.eINSTANCE.getElementAssignment_Assignment());

							failures.add(failure);
						}
					}
				} else { // FOD/DES cargo
					if (vessel instanceof Vessel) {
						if (scenarioVessels.contains(vessel)) {
							final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("None fleet cargo " + cargo.getName()
									+ " is assigned to scenario vessel " + vessel.getName() + "."));
							failure.addEObjectAndFeature(elementAssignment, AssignmentPackage.eINSTANCE.getElementAssignment_Assignment());
							failures.add(failure);
						}
					} else if (vessel != null) {
						final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("None fleet cargo " + cargo.getName()
								+ " can only be assigned to a specific vessel"));
						failure.addEObjectAndFeature(elementAssignment, AssignmentPackage.eINSTANCE.getElementAssignment_Assignment());

						failures.add(failure);
					}
				}
			} else if (slot != null) {
				boolean check = false;
				if (slot instanceof LoadSlot) {
					LoadSlot loadSlot = (LoadSlot) slot;
					check = loadSlot.isDESPurchase();
				} else if (slot instanceof DischargeSlot) {
					DischargeSlot dischargeSlot = (DischargeSlot) slot;
					check = dischargeSlot.isFOBSale();
				}
				if (check) {
					if (vessel instanceof Vessel) {
						if (scenarioVessels.contains(vessel)) {
							final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Slot " + slot.getName()
									+ " is assigned to scenario vessel " + vessel.getName() + "."));
							failure.addEObjectAndFeature(elementAssignment, AssignmentPackage.eINSTANCE.getElementAssignment_Assignment());
							failures.add(failure);
						}
					} else if (vessel != null) {
						final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Slot " + slot.getName()
								+ " can only be assigned to a specific vessel"));
						failure.addEObjectAndFeature(elementAssignment, AssignmentPackage.eINSTANCE.getElementAssignment_Assignment());
						failures.add(failure);
					}
				}
			}
		}

		return Activator.PLUGIN_ID;
	}

	private boolean isValidObject(EObject eObj) {
		boolean allowedObject = (eObj instanceof Cargo || eObj instanceof VesselEvent);

		if (!allowedObject && eObj instanceof LoadSlot) {
			LoadSlot loadSlot = (LoadSlot) eObj;
			allowedObject = loadSlot.isDESPurchase();
		}
		if (!allowedObject && eObj instanceof DischargeSlot) {
			DischargeSlot DischargeSlot = (DischargeSlot) eObj;
			allowedObject = DischargeSlot.isFOBSale();
		}
		return allowedObject;
	}
}
