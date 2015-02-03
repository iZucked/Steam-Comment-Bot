/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
/**
 # * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.assignment.validation.internal.Activator;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * Constraint to check the Assigned Vessel is in the allowed vessel list, if specified.
 * 
 */
public class AllowedVesselAssignmentConstraint extends AbstractModelMultiConstraint {

	private static final Logger log = LoggerFactory.getLogger(AllowedVesselAssignmentConstraint.class);

	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		if (object instanceof AssignableElement) {
			final AssignableElement assignableElement = (AssignableElement) object;

			final AVesselSet<? extends Vessel> vesselAssignment = assignableElement.getAssignment();
			if (vesselAssignment == null) {
				if (assignableElement instanceof VesselEvent) {
					final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("Vessel events must have a vessel assigned to them."));
					status.addEObjectAndFeature(assignableElement, CargoPackage.eINSTANCE.getAssignableElement_Assignment());
					failures.add(status);
					return Activator.PLUGIN_ID;
				} else
					return Activator.PLUGIN_ID;
			}

			// This will be a single vessel or a vessel class
			if (!(vesselAssignment instanceof Vessel || vesselAssignment instanceof VesselClass)) {
				// Unsupported case - bail out!
				log.error("Assignment is not a Vessel or VesselClass - unable to validate");
				return Activator.PLUGIN_ID;
			}

			final Set<EObject> targets = new HashSet<>();
			if (assignableElement instanceof Cargo) {
				final Cargo cargo = (Cargo) assignableElement;
				targets.addAll(cargo.getSlots());

			} else {
				targets.add(assignableElement);
			}
			boolean isThirdPartyCargo = false;
			for (final EObject target : targets) {
				EList<AVesselSet<Vessel>> allowedVessels = null;
				if (target instanceof Slot) {
					final Slot slot = (Slot) target;
					allowedVessels = slot.getAllowedVessels();

					if (slot instanceof LoadSlot) {
						isThirdPartyCargo = ((LoadSlot) slot).isDESPurchase();
					} else if (slot instanceof DischargeSlot) {
						isThirdPartyCargo = ((DischargeSlot) slot).isFOBSale();
					}

				} else if (target instanceof VesselEvent) {
					final VesselEvent vesselEvent = (VesselEvent) target;
					allowedVessels = vesselEvent.getAllowedVessels();
				}

				if (allowedVessels == null || allowedVessels.isEmpty()) {
					continue;
				}

				// Expand out VesselGroups
				final Set<AVesselSet<Vessel>> expandedVessels = new HashSet<AVesselSet<Vessel>>();
				for (final AVesselSet<Vessel> s : allowedVessels) {
					if (s instanceof Vessel) {
						expandedVessels.add(s);
					} else if (s instanceof VesselClass) {
						expandedVessels.add(s);
					} else {
						// This is ok as other impl (VesselGroup and VesselTypeGroup) only permit contained Vessels
						expandedVessels.addAll(SetUtils.getObjects(s));
					}
				}

				// Check that third party cargoes have a permitted third party vessel.
				if (isThirdPartyCargo && !allowedVessels.isEmpty()) {
					boolean foundThirdPartyVessel = false;

					final Set<Vessel> scenarioVessels = new HashSet<>();
					final MMXRootObject rootObject = extraContext.getRootObject();
					if (rootObject instanceof LNGScenarioModel) {
						final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
						final LNGPortfolioModel portfolioModel = lngScenarioModel.getPortfolioModel();
						final CargoModel cargoModel = portfolioModel.getCargoModel();
						for (final VesselAvailability va : cargoModel.getVesselAvailabilities()) {
							scenarioVessels.add(va.getVessel());
						}
					}

					for (final Vessel vessel : SetUtils.getObjects(allowedVessels)) {
						if (!scenarioVessels.contains(vessel)) {
							foundThirdPartyVessel = true;
							break;
						}
					}
					if (!foundThirdPartyVessel) {
						final String message;
						if (target instanceof Slot) {
							message = String.format("Slot '%s': Allowed vessels list does not permit any third party vessels to be assigned.", ((Slot) target).getName());
						} else {
							throw new IllegalStateException("Unexpected code branch.");
						}
						final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

						failure.addEObjectAndFeature(assignableElement, CargoPackage.eINSTANCE.getAssignableElement_Assignment());
						failure.addEObjectAndFeature(target, CargoPackage.eINSTANCE.getSlot_AllowedVessels());

						failures.add(failure);

					}
				}

				boolean permitted = false;
				if (expandedVessels.contains(vesselAssignment)) {
					permitted = true;
				} else if (vesselAssignment instanceof Vessel) {
					final Vessel vessel = (Vessel) vesselAssignment;
					for (final AVesselSet<Vessel> vs : expandedVessels) {
						if (vs instanceof VesselClass) {
							if (vs == vessel.getVesselClass()) {
								permitted = true;
								break;
							}
						}
					}
				}

				if (!permitted) {

					final String message;
					if (target instanceof Slot) {
						message = String.format("Slot '%s': Assignment '%s' is not in the allowed vessels list.", ((Slot) target).getName(), vesselAssignment.getName());
					} else if (target instanceof VesselEvent) {
						message = String.format("Vessel Event '%s': Assignment requires vessel(s) not in the allowed vessels list.", ((VesselEvent) target).getName());
					} else {
						throw new IllegalStateException("Unexpected code branch.");
					}
					final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

					failure.addEObjectAndFeature(assignableElement, CargoPackage.eINSTANCE.getAssignableElement_Assignment());
					if (target instanceof Cargo) {
						failure.addEObjectAndFeature(target, CargoPackage.eINSTANCE.getSlot_AllowedVessels());
					} else if (target instanceof VesselEvent) {
						failure.addEObjectAndFeature(target, CargoPackage.eINSTANCE.getVesselEvent_AllowedVessels());
					}

					failures.add(failure);
				}
			}
		}

		return Activator.PLUGIN_ID;
	}
}
