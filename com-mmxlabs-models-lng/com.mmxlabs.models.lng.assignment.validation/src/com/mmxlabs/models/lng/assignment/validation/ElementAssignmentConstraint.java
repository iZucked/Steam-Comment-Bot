/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.lng.types.util.ValidationConstants;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * Validation constraint to check the type of vessel assigned. FOB/DES cargoes
 * are either unassigned or a vessel not part of the "scenario" data - that is
 * vessels in the FleetModel but do not have a VesselAvailability. Fleet cargoes
 * can be only scenario vessel assignments. Vessel Events can only use scenario
 * vessels.
 * 
 */
public class ElementAssignmentConstraint extends AbstractModelMultiConstraint {
	@Override
	public void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		if (object instanceof AssignableElement ae) {
			final AssignableElement assignableElement = ae;

			Cargo cargo = null;
			if (assignableElement instanceof Cargo c) {
				cargo = c;
			}

			if (cargo != null && cargo.getCargoType() == CargoType.FLEET) {
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
				} else if (vesselAssignmentType instanceof CharterInMarket charterInMarket) {
					if (assignableElement.getSpotIndex() == -1) {

						if (!charterInMarket.isNominal()) {
							final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator(
									(IConstraintStatus) ctx
											.createFailureStatus("Cargo " + cargo.getLoadName() + " has nominal vessel assignment but the charter market does not permit nominal vessels."),
									IStatus.ERROR);
							failure.addEObjectAndFeature(assignableElement, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE);
							failure.setTag(ValidationConstants.TAG_NOMINAL_VESSELS);
							failures.add(failure);
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
}
