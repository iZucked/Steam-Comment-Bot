/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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

import com.mmxlabs.models.lng.assignment.validation.internal.Activator;
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
	
	final private void createErrorMessage(final String message, final IValidationContext ctx, List<IStatus> failures, EObject obj, final EStructuralFeature... features) {
		final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message, IStatus.ERROR));
		for(EStructuralFeature feature: features) {
			failure.addEObjectAndFeature(obj, feature);
		}
		failures.add(failure);	
	}
	
	final private boolean isAfterOrEquals(LocalDateTime a, LocalDateTime b) {
		return (a.isAfter(b) || a.isEqual(b));
	}

	final private boolean isBeforeOrEquals(LocalDateTime a, LocalDateTime b) {
		return (a.isBefore(b) || a.isEqual(b));
	}
	
	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		if (object instanceof VesselAvailability) {
			final VesselAvailability vesselAvailability = (VesselAvailability) object;
			final Vessel vessel = vesselAvailability.getVessel();
			
			if (vessel != null) {
				/**
				 *  Constraints check for min/max duration settings.
				 * */
				// Min duration can't be greater than max duration
				if (vesselAvailability.getAvailabilityOrContractMinDuration() != 0 && vesselAvailability.getAvailabilityOrContractMaxDuration() != 0) {
					if (vesselAvailability.getAvailabilityOrContractMaxDuration() < vesselAvailability.getAvailabilityOrContractMinDuration()) {
						final String message = String.format("Vessel|%s min duration is superior to the max duration.", vessel.getName());
						createErrorMessage(message, ctx, failures, vesselAvailability, CargoPackage.eINSTANCE.getVesselAvailability_MinDuration(), //
								CargoPackage.eINSTANCE.getVesselAvailability_MaxDuration(),
								CargoPackage.eINSTANCE.getCargoModel_VesselAvailabilities()
								);
					}
				}
				
				// Max duration centric checks 
				if (vesselAvailability.isSetMaxDuration()) {
					LocalDateTime startDate = null;

					// select start time if available
					if (vesselAvailability.isSetStartAfter()) {
						startDate = vesselAvailability.getStartAfter();
					} else if (vesselAvailability.isSetStartBy()) {
						startDate = vesselAvailability.getStartBy();
					}
					
					if (startDate != null) {
						// Compute the date corresponding to the max duration
						// Depending on the starting date
						final int maxDuration = vesselAvailability.getAvailabilityOrContractMaxDuration();
						LocalDateTime maxDate = startDate.plusDays(maxDuration);
					
						if (vesselAvailability.isSetEndBy() && vesselAvailability.isSetEndAfter()) {
							// IN BETWEEN 
							// The max date should be in between the endBy and the endAfter date
							if(!(isBeforeOrEquals(maxDate, vesselAvailability.getEndBy()) && isAfterOrEquals(maxDate, vesselAvailability.getEndAfter()))) {
								final String message = String.format("Vessel|%s max Duration is impossible to satisfy (Outside of EndAfter and EndBy range).", vessel.getName());
								createErrorMessage(message, ctx, failures, vesselAvailability, CargoPackage.eINSTANCE.getVesselAvailability_MaxDuration(), //
										CargoPackage.eINSTANCE.getVesselAvailability_EndAfter(), //
										CargoPackage.eINSTANCE.getVesselAvailability_EndBy());
							}
						} else if (vesselAvailability.isSetEndAfter()) {
							// AFTER
							// The max date should be set after the EndAfter date
							if (vesselAvailability.getEndAfter().isAfter(maxDate)) {
								final String message = String.format("Vessel|%s max Duration is impossible to satisfy (is before EndAfter).", vessel.getName());
								createErrorMessage(message, ctx, failures, vesselAvailability, CargoPackage.eINSTANCE.getVesselAvailability_MaxDuration(), //
										CargoPackage.eINSTANCE.getVesselAvailability_EndAfter());
							}
						} else if (vesselAvailability.isSetEndBy()) {
							// BEFORE
							// The max date should be set before the EndBy date
							if (vesselAvailability.getEndBy().isBefore(maxDate)) {
								final String message = String.format("Vessel|%s max Duration is impossible to satisfy (is after EndBy).", vessel.getName());
								createErrorMessage(message, ctx, failures, vesselAvailability, CargoPackage.eINSTANCE.getVesselAvailability_MaxDuration(), //
										CargoPackage.eINSTANCE.getVesselAvailability_EndBy());
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
					} else if (vesselAvailability.isSetStartBy()) {
						startDate = vesselAvailability.getStartBy();
					}
					
					if (startDate != null) {
						// Compute the date corresponding to the min duration
						// Depending on the ending date
						final int minDuration = vesselAvailability.getAvailabilityOrContractMinDuration();
						LocalDateTime minDate = startDate.plusDays(minDuration);

						if (vesselAvailability.isSetEndBy() && vesselAvailability.isSetEndAfter()) {
							// IN BETWEEN 
							// The min date should be in between the StartBy and the StartAfter date
							if(!(isBeforeOrEquals(minDate, vesselAvailability.getEndBy()))) {
								final String message = String.format("Vessel|%s min Duration is impossible to satisfy (Outside of EndAfter and EndBy range).", vessel.getName());
								createErrorMessage(message, ctx, failures, vesselAvailability, CargoPackage.eINSTANCE.getVesselAvailability_MinDuration(), //
										CargoPackage.eINSTANCE.getVesselAvailability_EndAfter(), //
										CargoPackage.eINSTANCE.getVesselAvailability_EndBy());
							}
						} else if (vesselAvailability.isSetEndAfter()) {
							// AFTER
							// The min date should be set after the StartAfter date
							if (!(minDate.isAfter(vesselAvailability.getEndAfter()) || vesselAvailability.getEndAfter().isEqual(minDate))) {
								final String message = String.format("Vessel|%s min Duration is impossible to satisfy (is before EndAfter).", vessel.getName());
								createErrorMessage(message, ctx, failures, vesselAvailability, CargoPackage.eINSTANCE.getVesselAvailability_MinDuration(), //
										CargoPackage.eINSTANCE.getVesselAvailability_EndAfter());
							}
						} else if (vesselAvailability.isSetEndBy()) {
							// BEFORE
							// The min date should be set before the StartBy date
							if (!(minDate.isBefore(vesselAvailability.getEndBy()) || vesselAvailability.getEndBy().isEqual(minDate))) {
								final String message = String.format("Vessel|%s min Duration is impossible to satisfy (is after EndBy).", vessel.getName());
								createErrorMessage(message, ctx, failures, vesselAvailability, CargoPackage.eINSTANCE.getVesselAvailability_MinDuration(), //
										CargoPackage.eINSTANCE.getVesselAvailability_EndBy());
							}
						}
					}
				}
			}
		}
		return Activator.PLUGIN_ID;
	}
}
