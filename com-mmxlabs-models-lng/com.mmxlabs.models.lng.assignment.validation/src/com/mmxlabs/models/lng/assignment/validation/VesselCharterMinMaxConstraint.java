/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation;

import java.time.LocalDateTime;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * Checks to test if slot and vessel event dates are consistent with the start /
 * end dates of the assigned vessel.
 * 
 */
public class VesselCharterMinMaxConstraint extends AbstractModelMultiConstraint {

	private final void createErrorMessage(final String message, final IValidationContext ctx, final List<IStatus> failures, final EObject obj, final EStructuralFeature... features) {
		final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message, IStatus.ERROR));
		for (final EStructuralFeature feature : features) {
			failure.addEObjectAndFeature(obj, feature);
		}
		failures.add(failure);
	}
 
	@Override
	public void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		if (object instanceof VesselCharter vesselCharter) {

			// Only check those in the main scenario
			if (vesselCharter.eContainer() instanceof CargoModel) {

				final Vessel vessel = vesselCharter.getVessel();

				if (vessel != null) {
					/**
					 * Constraints check for min/max duration settings.
					 */
					// Min duration can't be greater than max duration
					if (vesselCharter.getCharterOrDelegateMinDuration() != 0 && vesselCharter.getCharterOrDelegateMaxDuration() != 0) {
						if (vesselCharter.getCharterOrDelegateMaxDuration() < vesselCharter.getCharterOrDelegateMinDuration()) {
							final String message = String.format("Vessel|%s: Charter min duration is greater than max duration.", vessel.getName());
							createErrorMessage(message, ctx, failures, vesselCharter, CargoPackage.eINSTANCE.getVesselCharter_MinDuration(), //
									CargoPackage.eINSTANCE.getVesselCharter_MaxDuration(), CargoPackage.eINSTANCE.getCargoModel_VesselCharters());
						}
					}

					// Max duration centric checks
					if (vesselCharter.isSetMaxDuration()) {
						LocalDateTime startDate = null;

						if (vesselCharter.isSetStartBy()) {
							startDate = vesselCharter.getStartBy();
						}

						if (startDate != null) {
							// Compute the date corresponding to the max duration
							// Depending on the starting date
							final int maxDuration = vesselCharter.getCharterOrDelegateMaxDuration();
							final LocalDateTime maxDate = startDate.plusDays(maxDuration);

							if (vesselCharter.isSetEndAfter()) {
								// AFTER
								// The max date should be set after the EndAfter date
								if (vesselCharter.getEndAfter().isAfter(maxDate)) {
									final String message = String.format("Vessel|%s: Charter max duration of %d days is too short to cover charter start/end dates (%.1f days).", vessel.getName(),
											maxDuration, (float) Hours.between(vesselCharter.getStartBy(), vesselCharter.getEndAfter()) / 24.0f);
									createErrorMessage(message, ctx, failures, vesselCharter, CargoPackage.eINSTANCE.getVesselCharter_MaxDuration(), //
											CargoPackage.eINSTANCE.getVesselCharter_EndAfter());
								}

							}
						}
					}

					// Min duration centric checks
					if (vesselCharter.isSetMinDuration()) {
						LocalDateTime startDate = null;

						// select start time if available
						if (vesselCharter.isSetStartAfter()) {
							startDate = vesselCharter.getStartAfter();
						}

						if (startDate != null) {
							// Compute the date corresponding to the min duration
							// Depending on the ending date
							final int minDuration = vesselCharter.getCharterOrDelegateMinDuration();
							final LocalDateTime minDate = startDate.plusDays(minDuration);

							if (vesselCharter.isSetEndBy()) {
								if (!(minDate.isBefore(vesselCharter.getEndBy()) || vesselCharter.getEndBy().isEqual(minDate))) {
									final String message = String.format("Vessel|%s min duration is larger than the vessel is available for (%.1f days).", vessel.getName(),
											(float) Hours.between(vesselCharter.getStartAfter(), vesselCharter.getEndBy()) / 24.0f);
									createErrorMessage(message, ctx, failures, vesselCharter, CargoPackage.eINSTANCE.getVesselCharter_MinDuration(), //
											CargoPackage.eINSTANCE.getVesselCharter_EndBy());
								}
							}
						}
					}
				}
			}
		}
	}
}
