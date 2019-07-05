/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
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
import com.mmxlabs.models.lng.assignment.validation.internal.Activator;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * Checks to test if slot and vessel event dates are consistent with the start / end dates of the assigned vessel.
 * 
 */
public class VesselAvailabilityMinMaxConstraint extends AbstractModelMultiConstraint {

	final private void createErrorMessage(final String message, final IValidationContext ctx, final List<IStatus> failures, final EObject obj, final EStructuralFeature... features) {
		final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message, IStatus.ERROR));
		for (final EStructuralFeature feature : features) {
			failure.addEObjectAndFeature(obj, feature);
		}
		failures.add(failure);
	}

	final private boolean isAfterOrEquals(final LocalDateTime a, final LocalDateTime b) {
		return (a.isAfter(b) || a.isEqual(b));
	}

	final private boolean isBeforeOrEquals(final LocalDateTime a, final LocalDateTime b) {
		return (a.isBefore(b) || a.isEqual(b));
	}

	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		if (object instanceof VesselAvailability) {
			final VesselAvailability vesselAvailability = (VesselAvailability) object;

			// Only check those in the main scenario
			if (vesselAvailability.eContainer() instanceof CargoModel) {

				final Vessel vessel = vesselAvailability.getVessel();

				if (vessel != null) {
					/**
					 * Constraints check for min/max duration settings.
					 */
					// Min duration can't be greater than max duration
					if (vesselAvailability.getCharterOrDelegateMinDuration() != 0 && vesselAvailability.getCharterOrDelegateMaxDuration() != 0) {
						if (vesselAvailability.getCharterOrDelegateMaxDuration() < vesselAvailability.getCharterOrDelegateMinDuration()) {
							final String message = String.format("Vessel|%s min duration is superior to the max duration.", vessel.getName());
							createErrorMessage(message, ctx, failures, vesselAvailability, CargoPackage.eINSTANCE.getVesselAvailability_MinDuration(), //
									CargoPackage.eINSTANCE.getVesselAvailability_MaxDuration(), CargoPackage.eINSTANCE.getCargoModel_VesselAvailabilities());
						}
					}

					// Max duration centric checks
					if (vesselAvailability.isSetMaxDuration()) {
						LocalDateTime startDate = null;

						if (vesselAvailability.isSetStartBy()) {
							startDate = vesselAvailability.getStartBy();
						}

						if (startDate != null) {
							// Compute the date corresponding to the max duration
							// Depending on the starting date
							final int maxDuration = vesselAvailability.getCharterOrDelegateMaxDuration();
							final LocalDateTime maxDate = startDate.plusDays(maxDuration);

							if (vesselAvailability.isSetEndAfter()) {
								// AFTER
								// The max date should be set after the EndAfter date
								if (vesselAvailability.getEndAfter().isAfter(maxDate)) {
									final String message = String.format("Vessel|%s max duration is longer than minimum availability dates (%.1f days).", vessel.getName(),

											(float) Hours.between(vesselAvailability.getStartBy(), vesselAvailability.getEndAfter()) / 24.0f);
									createErrorMessage(message, ctx, failures, vesselAvailability, CargoPackage.eINSTANCE.getVesselAvailability_MaxDuration(), //
											CargoPackage.eINSTANCE.getVesselAvailability_EndAfter());
								}

							}
						}
					}

					// Min duration centric checks
					if (vesselAvailability.isSetMinDuration()) {
						LocalDateTime startDate = null;

						// select start time if available
						if (vesselAvailability.isSetStartAfter()) {
							startDate = vesselAvailability.getStartAfter();
						}

						if (startDate != null) {
							// Compute the date corresponding to the min duration
							// Depending on the ending date
							final int minDuration = vesselAvailability.getCharterOrDelegateMinDuration();
							final LocalDateTime minDate = startDate.plusDays(minDuration);

							if (vesselAvailability.isSetEndBy()) {
								if (!(minDate.isBefore(vesselAvailability.getEndBy()) || vesselAvailability.getEndBy().isEqual(minDate))) {
									final String message = String.format("Vessel|%s min duration is larger than the vessel is available for (%.1f days).", vessel.getName(),
											(float) Hours.between(vesselAvailability.getStartAfter(), vesselAvailability.getEndBy()) / 24.0f);
									createErrorMessage(message, ctx, failures, vesselAvailability, CargoPackage.eINSTANCE.getVesselAvailability_MinDuration(), //
											CargoPackage.eINSTANCE.getVesselAvailability_EndBy());
								}
							}
						}
					}
				}
			}
		}
		return Activator.PLUGIN_ID;
	}
}
