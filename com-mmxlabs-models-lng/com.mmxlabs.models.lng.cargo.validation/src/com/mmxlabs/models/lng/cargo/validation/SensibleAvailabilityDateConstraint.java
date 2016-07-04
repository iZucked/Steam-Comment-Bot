/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.time.LocalDateTime;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class SensibleAvailabilityDateConstraint extends AbstractModelMultiConstraint {

	private LocalDateTime earliestDate = LocalDateTime.of(2000, 1, 1, 0, 0);
	private EStructuralFeature[] availabilityDateFields = { CargoPackage.Literals.VESSEL_AVAILABILITY__END_AFTER, CargoPackage.Literals.VESSEL_AVAILABILITY__END_BY,
			CargoPackage.Literals.VESSEL_AVAILABILITY__START_AFTER, CargoPackage.Literals.VESSEL_AVAILABILITY__START_BY };

	/**
	 * Impose sensible date cutoffs for vessel availabilities and events
	 */
	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, List<IStatus> failures) {
		final EObject object = ctx.getTarget();
		if (object instanceof VesselAvailability) {
			final VesselAvailability vesselAvailability = (VesselAvailability) object;
			for (EStructuralFeature feature : availabilityDateFields) {
				final LocalDateTime date = (LocalDateTime) object.eGet(feature);
				if (date != null && date.isBefore(earliestDate)) {
					final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus(vesselAvailability.getVessel().getName(), feature.getName(), earliestDate.toString()));
					status.addEObjectAndFeature(object, feature);
					failures.add(status);
				}
			}

		}

		return Activator.PLUGIN_ID;
	}

}
