/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation;

import java.time.LocalDate;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.assignment.validation.internal.Activator;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.mmxcore.MMXRootObject;
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
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		if (object instanceof AssignableElement) {
			final AssignableElement assignableElement = (AssignableElement) object;

			Cargo cargo = null;
			if (assignableElement instanceof Cargo) {
				cargo = (Cargo) assignableElement;
			}

			if (cargo != null) {

				if (cargo.getCargoType() == CargoType.FLEET) {
					final VesselAssignmentType vesselAssignmentType = assignableElement.getVesselAssignmentType();
					if (vesselAssignmentType == null) {

						if (LicenseFeatures.isPermitted("features:require-vessel-allocation")) {
							final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator(
									(IConstraintStatus) ctx.createFailureStatus("Fleet cargo " + cargo.getLoadName() + " has no vessel assignment."), IStatus.ERROR);
							failure.addEObjectAndFeature(assignableElement, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE);

							failures.add(failure);

						} else {
							final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator(
									(IConstraintStatus) ctx.createFailureStatus("Fleet cargo " + cargo.getLoadName() + " has no vessel assignment. This may cause problems evaluating scenario."),
									IStatus.WARNING);
							failure.addEObjectAndFeature(assignableElement, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE);

							failures.add(failure);
						}
					} else if (vesselAssignmentType instanceof CharterInMarket) {
						CharterInMarket charterInMarket = (CharterInMarket) vesselAssignmentType;
						if (assignableElement.getSpotIndex() == -1) {
							final MMXRootObject rootObject = extraContext.getRootObject();
							if (rootObject instanceof LNGScenarioModel) {
								final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
								final LocalDate promptPeriodEnd = lngScenarioModel.getPromptPeriodEnd();
								if (promptPeriodEnd != null) {
									final List<Slot> sortedSlots = cargo.getSortedSlots();
									if (!sortedSlots.isEmpty()) {
										final Slot slot = sortedSlots.get(0);
										if (slot.getWindowStartWithSlotOrPortTime().toLocalDate().isBefore(promptPeriodEnd)) {
											final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator(
													(IConstraintStatus) ctx.createFailureStatus("Cargo " + cargo.getLoadName() + " has nominal vessel assignment in the prompt."), IStatus.WARNING);
											failure.addEObjectAndFeature(assignableElement, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE);

											failures.add(failure);
										}
									}
								}
							}
						} else if (!charterInMarket.isEnabled()) {
							final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator(
									(IConstraintStatus) ctx.createFailureStatus(String.format("Cargo %s is assigned to disabled market - %s.", cargo.getLoadName(), charterInMarket.getName())),
									IStatus.ERROR);
							failure.addEObjectAndFeature(assignableElement, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE);

							failures.add(failure);
						}
					}
				}
			}
		}

		return Activator.PLUGIN_ID;
	}
}
