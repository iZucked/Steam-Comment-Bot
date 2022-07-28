/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.ui.date.LocalDateTimeTextFormatter;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class SensibleVesselCharterDateConstraint extends AbstractModelMultiConstraint {

	private LocalDateTime earliestDate = LocalDateTime.of(2000, 1, 1, 0, 0);
	private EStructuralFeature[] vesselCharterDateFields = { CargoPackage.Literals.VESSEL_CHARTER__END_AFTER, CargoPackage.Literals.VESSEL_CHARTER__END_BY,
			CargoPackage.Literals.VESSEL_CHARTER__START_AFTER, CargoPackage.Literals.VESSEL_CHARTER__START_BY };

	/**
	 * Impose sensible date cutoffs for vessel charters and events
	 */
	@Override
	public void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, List<IStatus> failures) {
		final EObject object = ctx.getTarget();
		if (object instanceof VesselCharter vesselCharter) {
			for (EStructuralFeature feature : vesselCharterDateFields) {
				final LocalDateTime date = (LocalDateTime) object.eGet(feature);
				if (date != null && date.isBefore(earliestDate)) {
					final String msg = String.format("Vessel '%s': '%s' date is before %s.", vesselCharter.getVessel().getName(), feature.getName(), LocalDateTimeTextFormatter.format(earliestDate));
					final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
					status.addEObjectAndFeature(object, feature);
					failures.add(status);
				}
			}
		}
	}

}
